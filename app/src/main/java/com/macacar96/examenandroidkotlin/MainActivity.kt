package com.macacar96.examenandroidkotlin

import android.location.Address
import android.location.Geocoder
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.macacar96.examenandroidkotlin.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val db = FirebaseFirestore.getInstance()

    private val TIME_UPDATE_LOCATION: Long = 900000

    private var locationManager : LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_movie, R.id.nav_photo, R.id.nav_user, R.id.nav_location
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Crear una referencia de LocationManager
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

        // Al entrar a la App se manda a consultar e insertar ubicación
        requestLocationUpdates()

        // Despues se realiza lo mismo pero ahora cada 15 minutos = millis(900000)
        Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
            override fun run() {
                requestLocationUpdates()
                Handler(Looper.getMainLooper()).postDelayed(this, TIME_UPDATE_LOCATION)
            }
        }, TIME_UPDATE_LOCATION)
    }

    // Función para mandar a escuchar localización
    private fun requestLocationUpdates() {
        try {
            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0L,
                0f,
                locationListener
            )
        } catch(ex: SecurityException) {
            Log.d("LOG-LOCATION", "Excepción de seguridad, no hay ubicación disponible")
        }
    }

    // Escuchar ubicación y guardarla en firestore
    private val locationListener: LocationListener = LocationListener { location ->

        val geocoder = Geocoder(this)
        val list = geocoder.getFromLocation(location.latitude, location.longitude, 1)

        db.collection("locations").add(
            hashMapOf(
                "latitude" to location.latitude.toString(),
                "longitude" to location.longitude.toString(),
                "direction" to list[0].getAddressLine(0)
            )
        )
        Log.d("LOG-LOOP", "HECHO!!!")
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}