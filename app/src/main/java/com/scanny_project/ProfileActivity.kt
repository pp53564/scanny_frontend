package com.scanny_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.ui_ux_demo.R
import com.example.ui_ux_demo.databinding.ActivityProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.scanny_project.data.SessionManager
import com.scanny_project.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding
    lateinit var navigation : BottomNavigationView
    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val username = sharedPref.getString("username", "User")

//        binding.tvUsername.text = username

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
          logoutUser()
        }

    }

    private fun logoutUser() {
        sessionManager.clearSession()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        navigation.selectedItemId = R.id.profile
    }

}