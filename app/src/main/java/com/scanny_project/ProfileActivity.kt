package com.scanny_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ui_ux_demo.R
import com.example.ui_ux_demo.databinding.ActivityProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding
    lateinit var navigation : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigation = binding.root.findViewById(R.id.bottomNavigationView)

        navigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.profile -> true
                R.id.camera -> {
                    startActivity(Intent(applicationContext, CameraActivity::class.java))
                    true
                }
                else -> false
            }   }
        navigation.selectedItemId = R.id.profile
    }

    override fun onResume() {
        super.onResume()
        navigation.selectedItemId = R.id.profile
    }

}