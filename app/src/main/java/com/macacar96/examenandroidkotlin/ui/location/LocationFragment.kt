package com.macacar96.examenandroidkotlin.ui.location

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.macacar96.examenandroidkotlin.utils.NotificationDialog
import com.macacar96.examenandroidkotlin.R


class LocationFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    // Instancia de Firestore
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_location, container, false)

        // Soportamos el componente del Fragment que contiene el mapa
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Control del mapa
        mMap = googleMap

        // Leer y mostrar las coodenadas guardadas en FireStore
        db.collection("locations")
            .get()
            .addOnSuccessListener { result ->
                lateinit var coordinatesZoom: LatLng
                if (!result.isEmpty) {
                    for (document in result) {
                        mMap.addMarker(
                            MarkerOptions()
                                .position(
                                    LatLng(
                                        document.data["latitude"].toString().toDouble(),
                                        document.data["longitude"].toString().toDouble()
                                    )
                                )
                                .title(document.data["direction"].toString())
                        )

                        // Atrapa última coordenada para el animatedCamera con Zoom
                        coordinatesZoom = LatLng(
                            document.data["latitude"].toString().toDouble(),
                            document.data["longitude"].toString().toDouble()
                        )
                    }

                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(coordinatesZoom, 16f),
                        4000,
                        null
                    )

                } else {
                    // Mostrar el problema al user
                    NotificationDialog(
                        "Upps!",
                        "Aún no se han registrado ubicaciones en el servidor"
                    ).show(
                        childFragmentManager,
                        NotificationDialog.TAG
                    )
                }

            }
            .addOnFailureListener { exception ->
                // Error
                Log.d("FIRESTORE-ERROR", "Error getting documents: ", exception)
                NotificationDialog(
                    "Upps!",
                    "Estamos experimentando problemas con el servidor, intente mas tarde por favor"
                ).show(
                    childFragmentManager,
                    NotificationDialog.TAG
                )
            }
    }

}


