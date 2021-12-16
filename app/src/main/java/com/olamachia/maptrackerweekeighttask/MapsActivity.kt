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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import com.olamachia.maptrackerweekeighttask.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var locationManager: LocationManager
    private var marker1 : Marker? = null

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
            getPartnerLocation()
        }


    }


    //initialise map
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        getCurrentLocation()


    }


    companion object {
        private const val TAG = "MapsActivity" // for debugging
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        // use location manager to manage location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 2f) { location ->
            val latLng = LatLng(location.latitude, location.longitude)

            // toast to confirm latitude updates
           Toast.makeText(this,"${location.latitude}",Toast.LENGTH_LONG).show()
            map.clear()
            //// get your current location from location manager,add marker and set to contact image
            marker1 = map.addMarker(
                MarkerOptions().position(latLng).title("you are currently here")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.contact))
            )
            marker1?.position = latLng
            //add current location to firebase
            reference.child("Hakeem").setValue(location)
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            Log.d(TAG, "your location")

            Toast.makeText(
                applicationContext,
                "this is your location",
                Toast.LENGTH_LONG
            ).show()
        }


    }

//    private fun getPartnerLocation() {
//        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
//        val reference: DatabaseReference = database.getReference("Dubem")
//
//        reference.addValueEventListener(
//            object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val partnerModel = snapshot.getValue(LocationInfo::class.java)
//                    val latLng = LatLng(partnerModel?.latitude!!, partnerModel.longitude!!)
//                    marker2 = map.addMarker(MarkerOptions().position(latLng).title("dubem is here"))
//                    marker1?.position = latLng
//                    // Create an object that will specify how the camera will be updated
//                    val update = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f)
//                    map.moveCamera(update)
//                    Log.d(TAG, "Locations accessed from the database")
//
//                    Toast.makeText(
//                        applicationContext,
//                        "Locations accessed from the database",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    Log.d(TAG, "Could not read from database")
//                    Toast.makeText(
//                        applicationContext,
//                        "Could not read from database",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            }
//        )
//    }

    private fun getPartnerLocation (){
        // get partner location and your current location from database and add markers to map
        reference.addValueEventListener(
            object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var partnerModel = snapshot.child("Dubem").getValue(LocationInfo::class.java)
                    var partnerLatLng = LatLng(partnerModel?.latitude!!,partnerModel.longitude!!)
                    map.clear()
                    map.addMarker(
                        MarkerOptions().position(partnerLatLng)
                            .title("Dubem is currently here!"))

                    // Create an object that will specify how the camera will be updated
                    val update = CameraUpdateFactory.newLatLngZoom(partnerLatLng, 16.0f)
                    map.moveCamera(update)

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
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MapsActivity,"Cannot get partner's location",Toast.LENGTH_SHORT).show()
                }
            }
        )

    }


}