package com.olamachia.maptrackerweekeighttask

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.olamachia.maptrackerweekeighttask.databinding.ActivityMapsBinding
import com.olamachia.maptrackerweekeighttask.databinding.ActivitySplashScreenBinding


class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var ivLocation = binding.ivLocation
        ivLocation.alpha = 0f
        ivLocation.animate().setDuration(1500).alpha(1f).withEndAction {
            val i  = Intent(this, MainActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()
        }

    }
}