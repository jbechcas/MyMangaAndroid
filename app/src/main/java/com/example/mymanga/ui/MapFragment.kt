package com.example.mymanga.ui
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mymanga.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createFragment()
    }

    private fun createFragment() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.xml_fragment_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        createMarker()

    }

    private fun createMarker() {
        val myLocationPlace = LatLng(40.4165, -3.70256)
        map.addMarker(MarkerOptions().position(myLocationPlace).title("Mi ubicación"))


        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(myLocationPlace, 10f),
            4000,
            null
        )

    }
}
