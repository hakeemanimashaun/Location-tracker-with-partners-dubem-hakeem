package com.olamachia.maptrackerweekeighttask

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.olamachia.maptrackerweekeighttask.databinding.ActivityMainBinding
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var requestID = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //request permission on click of button and open map activity
        binding.currentLocation.setOnClickListener {
            requestPermission()
            val intent = Intent(applicationContext, MapsActivity::class.java)
            startActivity(intent)
        }



    }

    private fun requestPermission(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), requestID)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == requestID){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("MainActivity", "permission granted")
                Toast.makeText(
                    applicationContext,
                    "permission granted",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
