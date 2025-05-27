package com.scanny_project.features.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.ui_ux_demo.databinding.ActivityMainBinding
import com.scanny_project.data.SessionManager
import com.scanny_project.features.teacher.home.TeacherHomeActivity
import com.scanny_project.utils.TextToSpeechHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var ttsHelper: TextToSpeechHelper
    @Inject
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.startButton.setOnClickListener {
//            val intent = Intent(this, HomeActivity::class.java)
//            startActivity(intent)
            val nextScreen = if (sessionManager.userRole == "ROLE_TEACHER") {
                Intent(this, TeacherHomeActivity::class.java)
            } else {
                Intent(this, HomeActivity::class.java)
            }
            startActivity(nextScreen)
        }

        val sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val username = sharedPref.getString("username", "User")

        ttsHelper = TextToSpeechHelper(this) {
            val message = "Bok $username"
            ttsHelper.speak(message)
        }

    }

}


