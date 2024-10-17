package com.example.kivunavaidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.kivunavaidapp2.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

class MainActivity : ComponentActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap // Declare googleMap as a member variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            mapView = MapView(this)
            mapView.onCreate(savedInstanceState)
            mapView.getMapAsync(this)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap // Assign the parameter to the member variable
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-1.286389, 36.817223), 12f))
        parseKML()
    }

    private fun parseKML() {
        val kmlInputStream = resources.openRawResource(R.raw.sample_kml)
        val factory = XmlPullParserFactory.newInstance()
        val parser: XmlPullParser = factory.newPullParser()
        parser.setInput(kmlInputStream, null)

        var eventType = parser.eventType
        var coordinates = ""

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && parser.name == "coordinates") {
                coordinates = parser.nextText()
                break
            }
            eventType = parser.next()
        }

        // Split coordinates and add markers
        coordinates.split(" ").forEach { coord ->
            val (lng, lat) = coord.split(",")
            googleMap.addMarker(MarkerOptions().position(LatLng(lat.toDouble(), lng.toDouble())))
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}
