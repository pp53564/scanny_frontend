//package com.scanny_project
//
//import android.content.Intent
//import android.content.SharedPreferences
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import com.example.ui_ux_demo.R
//import com.scanny_project.data.SessionManager
//import com.scanny_project.features.home.HomeActivity
//import com.scanny_project.features.login.LoginActivity
//import dagger.hilt.android.AndroidEntryPoint
//import javax.inject.Inject
//
//@AndroidEntryPoint
//class SplashActivity : AppCompatActivity() {
//    private val splashTimeOut: Long = 3000
//    private lateinit var preferences: SharedPreferences
//    @Inject
//    lateinit var sessionManager: SessionManager
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_splash)
//
//
//        preferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
//
//        val isFirstLaunch = preferences.getBoolean("isFirstLaunch", true)
//
//        if (isFirstLaunch) {
//            setContentView(R.layout.activity_splash)
//            Handler(Looper.getMainLooper()).postDelayed({
//                /* startActivity(Intent(this, MainActivity::class.java))*/
//
//                /*  val options = ActivityOptions.makeCustomAnimation(
//                      applicationContext,
//                      R.anim.slide_in_right,  // Apply the custom slide-in-right animation
//                      R.anim.slide_out_left   // Apply the custom slide-out-left animation
//                  )
//                  startActivity(Intent(this, MainActivity::class.java), options.toBundle())*/
//
////                startActivity(Intent(this, MainActivity::class.java))
////                startActivity(Intent(this, LoginActivity::class.java))
//                checkLoginStatus()
//                finish()
//            }, splashTimeOut)
//        } else {
////            val mainIntent = Intent(this@SplashActivity, MainActivity::class.java)
////            startActivity(Intent(this, LoginActivity::class.java))
////            startActivity(mainIntent)
//            checkLoginStatus()
//            finish()
//        }
//    }
//
//    private fun checkLoginStatus() {
//        if (sessionManager.authToken != null) {
//            startActivity(Intent(this, HomeActivity::class.java))
//        } else {
//            startActivity(Intent(this, LoginActivity::class.java))
//        }
//        finish()
//    }
//}