package com.kenargo.mapwidgetsampleapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.kenargo.map_widgets.Interfaces
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    private var marker: Marker? = null

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val someLocation = LatLng(47.60357, -122.32945)

        marker = mMap.addMarker(MarkerOptions().position(someLocation).title("Seattle, Washington"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(someLocation))

        widgetLocationCoordinate.setCoordinates(someLocation.latitude, someLocation.longitude)

        widgetLocationCoordinate.setOnLocationChange(Interfaces.MapLocationUpdateChange {
            marker!!.position = LatLng(it!!.latitude, it.longitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(marker!!.position))
        })
    }
}
