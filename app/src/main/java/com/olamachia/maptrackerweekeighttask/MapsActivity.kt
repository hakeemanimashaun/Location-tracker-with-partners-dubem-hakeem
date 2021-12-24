package com.olamachia.maptrackerweekeighttask

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import com.olamachia.maptrackerweekeighttask.Model.LocationInfo
import com.olamachia.maptrackerweekeighttask.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var locationManager: LocationManager


    private var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var reference = firebaseDatabase.getReference("Partners")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //initialise location manager
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        binding.btnFindLocation.setOnClickListener {
            getAllLocation()
        }


    }


    //initialise map
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        saveCurrentLocation()


    }


    companion object {
        private const val TAG = "MapsActivity" // for debugging
    }

    @SuppressLint("MissingPermission")
    private fun saveCurrentLocation() {
        // use location manager to manage location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 2f) { location ->
            val latLng = LatLng(location.latitude, location.longitude)

            // toast to confirm latitude updates
           Toast.makeText(this,"${location.latitude}",Toast.LENGTH_LONG).show()
            map.clear()

            //add current location to firebase
            reference.child("Hakeem").setValue(location)


            Toast.makeText(
                applicationContext,
                "location added to database",
                Toast.LENGTH_LONG
            ).show()
            Log.d(TAG, "location saved")
        }


    }

    private fun getAllLocation (){
        // get partner location and your current location from database and add markers to map
        reference.addValueEventListener(
            object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // get partner location from database using location info model for path dubem
                    var partnerModel = snapshot.child("Dubem").getValue(LocationInfo::class.java)
                    var partnerLatLng = LatLng(partnerModel?.latitude!!,partnerModel.longitude!!)

                    map.clear()

                    // add location maker
                    map.addMarker(
                        MarkerOptions().position(partnerLatLng)
                            .title("Dubem is currently here!"))

                    // Create an object that will specify how the camera will be updated
                    val update = CameraUpdateFactory.newLatLngZoom(partnerLatLng, 16.0f)
                    map.moveCamera(update)
                    Log.d(TAG, "partner location retrieved")

                    // get my location from database using location info model for path Hakeem
                    var myModel = snapshot.child("Hakeem").getValue(LocationInfo::class.java)
                    var myLatLng = LatLng(myModel?.latitude!!,myModel.longitude!!)

                    //.. add marker and set marker to image
                    map.addMarker(
                        MarkerOptions().position(myLatLng)
                            .title("Hakeem is currently here!").icon(BitmapDescriptorFactory.fromResource(R.drawable.contact))
                    )

                    // Create an object that will specify how the camera will be updated
                    val update1 = CameraUpdateFactory.newLatLngZoom(myLatLng, 16.0f)
                    map.moveCamera(update1)
                    Log.d(TAG, "self location retrieved")
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MapsActivity,"Cannot get partner's location",Toast.LENGTH_SHORT).show()
                }
            }
        )

    }


}