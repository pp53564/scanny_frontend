package com.scanny_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.ui_ux_demo.R
import com.example.ui_ux_demo.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.skydoves.transformationlayout.TransformationCompat
import com.skydoves.transformationlayout.TransformationLayout
import com.skydoves.transformationlayout.onTransformationStartContainer

class HomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    lateinit var navigation : BottomNavigationView
    private lateinit var transformationLayout: TransformationLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationStartContainer()
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigation = binding.root.findViewById(R.id.bottomNavigationView)
        transformationLayout = binding.transformationLayout

        navigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> true
                R.id.profile -> {
                    startActivity(Intent(applicationContext, ProfileActivity::class.java))
                    true
                }
                R.id.camera -> {
                    startActivity(Intent(applicationContext, CameraActivity::class.java))
                    true
                }
                else -> false
            }   }
            navigation.selectedItemId = R.id.home


        binding.cvScanning.setOnClickListener {
            /*startActivity(Intent(this, CameraActivity::class.java))*/
            TransformationCompat.startActivity(transformationLayout, Intent(this, CameraActivity::class.java))
        }
      /*  binding.cvScanning.setOnClickListener {
            val navController = findNavController(R.id.fragment_container)
            navController.navigate(R.id.tutorialCamera1Fragment)
        }*/
    }

    override fun onResume() {
        super.onResume()
        navigation.selectedItemId = R.id.home
    }
}