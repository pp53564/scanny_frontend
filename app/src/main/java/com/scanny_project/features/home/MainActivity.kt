package com.scanny_project.features.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.ui_ux_demo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.startButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
           /* val options = ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_right,  // Slide-in animation
                R.anim.slide_out_left   // Slide-out animation
            )
*/
            /*startActivity(intent, options.toBundle())*/
            startActivity(intent)
        }

    }

}


