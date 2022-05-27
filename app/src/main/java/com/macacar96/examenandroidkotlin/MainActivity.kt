package com.macacar96.examenandroidkotlin

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.macacar96.examenandroidkotlin.databinding.ActivityMainBinding
import com.macacar96.examenandroidkotlin.ui.user.UserApp
import com.macacar96.examenandroidkotlin.utils.NotificationDialog
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    // Instancia de Firestore
    private val db = FirebaseFirestore.getInstance()

    private var locationManager: LocationManager? = null

    companion object {
        const val REQUEST_CODE_LOCATION = 0

        // Mínimo tiempo para updates en milisegundos = 15 minutos
        const val MIN_TIEMPO_ENTRE_UPDATES: Long = (1000 * 60 * 15)

        // Mínima distancia para updates en metros.
        const val MIN_CAMBIO_DISTANCIA_PARA_UPDATES: Float = 1.5f
    }

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

        // Check permisos de ubicación
        isLocationEnabled()

        // Al entrar a la App se manda a consultar e insertar ubicación cada 15 min
        requestLocationUpdates()
    }

    // Validate permiso
    private fun isLocationEnabled() {
        if (!isLocationPermissionGranted()) {
            // Si aún no tiene permisos
            requestLocationPermission()
        }
    }

    // Check si tiene permisos aceptados
    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    // Pedir permisos o agregarlos manualmente
    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            // Ya se pidieron los permisos pero se rechazaron
            Toast.makeText(this, "Ve a ajustes y acepta los permisos", Toast.LENGTH_LONG).show()
        } else {
            // Apenas se pedirán los permisos
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION
            )
        }
    }

    // Resultado de permiso aceptado o rechazado
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(
                    this,
                    "Para activar la localización ve a ajustes y acepte los permisos",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(this, "Permisos concedidos...", Toast.LENGTH_SHORT).show()
                requestLocationUpdates()
            }
        }
    }

    // Función para mandar a escuchar localización
    private fun requestLocationUpdates() {
        try {
            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIEMPO_ENTRE_UPDATES,
                MIN_CAMBIO_DISTANCIA_PARA_UPDATES,
                locationListener,
                Looper.getMainLooper()
            )
            Toast.makeText(this, "Obteniendo ubicación...", Toast.LENGTH_LONG).show()
        } catch (ex: SecurityException) {
            Log.d("LOG-LOCATION", "Excepción de seguridad, no hay ubicación disponible")
            // Notificar al usuario - warning
            Toast.makeText(this, "No hay ubicación disponible", Toast.LENGTH_SHORT).show()
        }
    }

    // Escuchar ubicación y guardarla en firestore
    private val locationListener: LocationListener = LocationListener { location ->

        // Obtener dirección a través de LatLong
        val geocoder = Geocoder(this)
        val list = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        val direction: String = list[0].getAddressLine(0)

        // Guardar datos en FireStore
        db.collection("locations").add(
            hashMapOf(
                "latitude" to location.latitude.toString(),
                "longitude" to location.longitude.toString(),
                "direction" to direction,
                "created_at" to Timestamp.now()
            )
        )

        Log.d("saved_location", "La localización ha sido guardada en FireStore")

    }

    override fun onResume() {
        super.onResume()
        isLocationEnabled()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}