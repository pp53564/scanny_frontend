package com.scanny_project.features.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import com.example.ui_ux_demo.R
import com.example.ui_ux_demo.databinding.ActivityProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.scanny_project.data.SessionManager
import com.scanny_project.features.home.HomeActivity
import com.scanny_project.features.home.MainActivity
import com.scanny_project.features.language.SelectLanguageActivity
import com.scanny_project.features.login.LoginActivity
import com.scanny_project.features.teacher.home.TeacherHomeActivity
import com.scanny_project.utils.setupNavigation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding
    lateinit var navigation : BottomNavigationView
    private val viewModel: ProfileViewModel by viewModels()
    @Inject
    lateinit var sessionManager: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val username = sharedPref.getString("username", "User")

        binding.tvUsername.text = username
        binding.header.titleText.text = getString(R.string.profile)

        binding.header.buttonBackLayout.imButtonBack.setOnClickListener {
//            val intent = Intent(this, HomeActivity::class.java)
//            startActivity(intent)
            onBackPressedDispatcher.onBackPressed()
        }

        navigation = binding.root.findViewById(R.id.bottomNavigationView)
        navigation.setupNavigation(
            mapOf(
                R.id.home to {
                    val homeCls = if (sessionManager.isTeacher)
                        TeacherHomeActivity::class.java
                    else
                        HomeActivity::class.java
                    startActivity(Intent(this, homeCls))
                    true },
                R.id.camera to {
                    Intent(this, SelectLanguageActivity::class.java).also {
                        it.putExtra(SelectLanguageActivity.EXTRA_MODE,
                            SelectLanguageActivity.MODE_CAPTURE
                        )
                        startActivity(it)
                    } },
                R.id.profile to { true }
            )
        )
        navigation.selectedItemId = R.id.profile

         binding.cvLogout.setOnClickListener {
            viewModel.logout()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
//        binding.clChangePassword.setOnClickListener {
//            val intent = Intent(this, ChangePasswordActivity::class.java)
//            startActivity(intent)
//        }

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