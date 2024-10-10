package com.scanny_project

import android.app.ActivityOptions
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.example.ui_ux_demo.R

class SplashActivity : AppCompatActivity() {
    private val splashTimeOut: Long = 3000
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        preferences = getSharedPreferences("app_preferences", MODE_PRIVATE)

        // Check if it's the first launch
        val isFirstLaunch = preferences.getBoolean("isFirstLaunch", true)

        if (isFirstLaunch) {
            // If it's the first launch, show the splash screen
            setContentView(R.layout.activity_splash)
            Handler(Looper.getMainLooper()).postDelayed({
                /* startActivity(Intent(this, MainActivity::class.java))*/

                /*  val options = ActivityOptions.makeCustomAnimation(
                      applicationContext,
                      R.anim.slide_in_right,  // Apply the custom slide-in-right animation
                      R.anim.slide_out_left   // Apply the custom slide-out-left animation
                  )
                  startActivity(Intent(this, MainActivity::class.java), options.toBundle())*/

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, splashTimeOut)
        } else {
            val mainIntent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }
    }
}