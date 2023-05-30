package com.anis.masglobalchallenge.ui



import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.anis.masglobalchallenge.R
import com.anis.masglobalchallenge.databinding.FragmentWeatherBinding
import com.anis.masglobalchallenge.viewmodel.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class WeatherFragment : Fragment(), MenuProvider {
    private var _binding: FragmentWeatherBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    @Inject lateinit var weatherViewModel: WeatherViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        multiplePermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                weatherViewModel.uiState.collect { weatherUiState ->
                    val temperature = weatherUiState.main.temp
                    val temperaturePlaceholder = getString(R.string.temperature_placeholder)
                    binding.let {
                        it.temperature.text =
                            if (temperature != null) temperature + getString(R.string.degree_symbol)
                            else temperaturePlaceholder
                        it.description.text = weatherUiState.weather.description ?: temperaturePlaceholder
                        it.maxTemp.text=  weatherUiState.main.temp_max ?: temperaturePlaceholder
                        it.minTemp.text = weatherUiState.main.temp_min ?: temperaturePlaceholder
                        it.icon.load("https://openweathermap.org/img/wn/${weatherUiState.weather.icon}@2x.png")

                    }

                }
            }
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token
            override fun isCancellationRequested() = false
        }).addOnSuccessListener { location: Location? ->
            if (location == null)
                Toast.makeText(requireActivity(), "Cannot get location.", Toast.LENGTH_SHORT).show()
            else {
                weatherViewModel.fetchWeather(lat = location.latitude.toString(), lon= location.longitude.toString())
                weatherViewModel.uiState.value.location = true
            }

        }

    }

    private val multiplePermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        var location = weatherViewModel.uiState.value.location
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
                if (!location) getLocation()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                if (!location) getLocation()
            }
            else -> {
                // No location access granted.
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_weather_search, menu)
        val searchItem: MenuItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem.actionView as? SearchView
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(city: String): Boolean {
                weatherViewModel.fetchWeather(city = city)
                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        TODO("Not yet implemented")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}