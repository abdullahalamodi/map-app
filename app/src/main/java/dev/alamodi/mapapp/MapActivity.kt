package dev.alamodi.mapapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private val mapViewModel: MapViewModel by lazy {
        ViewModelProviders.of(this).get(MapViewModel::class.java)
    }
    private lateinit var categoriesList: Array<String>
    private  var currentLocation: Location? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()
        categoriesList = Location.getCategoriesList()
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            0,
            0
        )
        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        categoriesList = Location.getCategoriesList()
        navView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            when (menuItem.itemId) {
                R.id.nav_current -> {
                    if (currentLocation != null){
                        loadLocations(CURRENT_LOCATION)
                        setActionBarTitle(CURRENT_LOCATION)
                    }else{
                        fetchLocation()
                    }
                }
                R.id.nav_schools -> {
                    loadLocations(categoriesList[0])
                    setActionBarTitle(categoriesList[0])
                }
                R.id.nav_restaurants -> {
                    loadLocations(categoriesList[1])
                    setActionBarTitle(categoriesList[1])
                }
                R.id.nav_hotels -> {
                    loadLocations(categoriesList[2])
                    setActionBarTitle(categoriesList[2])
                }
                R.id.nav_markets -> {
                    loadLocations(categoriesList[3])
                    setActionBarTitle(categoriesList[3])
                }
                R.id.nav_stadiums -> {
                    loadLocations(categoriesList[4])
                    setActionBarTitle(categoriesList[4])
                }
            }

            true
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchLocation() {
        checkPermissions()
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = Location(
                    category = CURRENT_LOCATION,
                    title = "my location",
                    lat = location.latitude,
                    lng = location.longitude
                )
                loadLocations(CURRENT_LOCATION)
            } else{
                Toast.makeText(baseContext,"current location not allowed",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), REQUEST_CODE
            )
            return
        }
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }


    override fun onMapReady(map: GoogleMap?) {
        googleMap = map!!
    }

    private fun loadLocations(category: String) {
        googleMap.clear()
        if (category == CURRENT_LOCATION) {
            currentLocation?.let {
            displayMarkers(listOf(it))
            }
        } else {
            mapViewModel.getLocations(category).observe(
                this, Observer { locations ->
                    if (locations.isNotEmpty())
                        displayMarkers(locations)
                }
            )
        }
    }


    private fun displayMarkers(locations: List<Location>) {
        locations.forEach { location ->
            val latLng = LatLng(location.lat, location.lng)
            googleMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(location.title)
                    .snippet(location.snippet)
            )
        }
        val latLng = LatLng(locations[0].lat, locations[0].lng)
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f))

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation()
                }
            }
        }
    }

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, MapActivity::class.java)
        }

        private const val REQUEST_CODE = 11
        private const val CURRENT_LOCATION = "current location"
    }
}