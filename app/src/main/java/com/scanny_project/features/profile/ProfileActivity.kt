package com.scanny_project.features.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import com.example.ui_ux_demo.R
import com.example.ui_ux_demo.databinding.ActivityProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.scanny_project.features.camera.CameraActivity
import com.scanny_project.features.home.HomeActivity
import com.scanny_project.features.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding
    lateinit var navigation : BottomNavigationView
      private val viewModel: ProfileViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val username = sharedPref.getString("username", "User")

        binding.tvUsername.text = username
        binding.header.titleText.text = getString(R.string.profile)

        binding.header.buttonBackLayout.imButtonBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

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

        findViewById<TextView>(R.id.tvLogout).setOnClickListener {
            viewModel.logout()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

    }

//    private fun logoutUser() {
//        sessionManager.clearSession()
//
//        val intent = Intent(this, LoginActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        startActivity(intent)
//        finish()
//    }

    override fun onResume() {
        super.onResume()
        navigation.selectedItemId = R.id.profile
    }

}