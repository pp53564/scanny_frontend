package com.scanny_project.features.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ui_ux_demo.R
import com.example.ui_ux_demo.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.scanny_project.features.language.SelectLanguageActivity
import com.scanny_project.features.language.SelectLanguageActivityForImageClassification
import com.scanny_project.features.tutorial.TutorialActivity
import com.scanny_project.data.SessionManager
import com.scanny_project.features.profile.ProfileActivity
import com.scanny_project.features.stats.StatsActivity
import com.scanny_project.utils.setupNavigation
import com.skydoves.transformationlayout.onTransformationStartContainer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    lateinit var navigation : BottomNavigationView
//    private lateinit var transformationLayout: TransformationLayout
//    private lateinit var transformationLayoutQuiz: TransformationLayout
    @Inject
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationStartContainer()
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigation = binding.root.findViewById(R.id.bottomNavigationView)
        val sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
//        val username = sharedPref.getString("username", "User")
//        binding.tvUsername.text = "Bok $username"

        navigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> true
                R.id.profile -> {
                    startActivity(Intent(applicationContext, ProfileActivity::class.java))
                    true
                }
                R.id.camera -> {
                    startActivity(Intent(applicationContext, SelectLanguageActivity::class.java))
                    true
                }
                else -> false
            }
        }

        navigation.setupNavigation(
            mapOf(
                R.id.home to { true },
                R.id.camera to { startActivity(Intent(this, SelectLanguageActivity::class.java)) },
                R.id.profile to {
                    startActivity(Intent(applicationContext, ProfileActivity::class.java))
                    true
                }
            )
        )

        navigation.selectedItemId = R.id.home

        binding.cvScanning.setOnClickListener {
            startActivity(Intent(this, SelectLanguageActivity::class.java))
//            TransformationCompat.startActivity(transformationLayout, Intent(this, CameraActivity::class.java))
        }
        binding.cvQuiz.setOnClickListener {
//         TransformationCompat.startActivity(transformationLayoutQuiz, Intent(this, LecturesListActivity::class.java))
            startActivity(Intent(this, SelectLanguageActivityForImageClassification::class.java))
        }

        binding.cvTutorial.setOnClickListener {
//            val navController = findNavController(R.id.fragment_container_tutorial)
//            navController.navigate(R.id.tutorialCamera1Fragment)
            startActivity(Intent(this, TutorialActivity::class.java))
        }

        binding.cvStats.setOnClickListener {
            startActivity(Intent(this, StatsActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        navigation.selectedItemId = R.id.home
    }
}