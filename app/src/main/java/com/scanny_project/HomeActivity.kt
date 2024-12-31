package com.scanny_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ui_ux_demo.R
import com.example.ui_ux_demo.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.scanny_project.data.SessionManager
import com.skydoves.transformationlayout.TransformationCompat
import com.skydoves.transformationlayout.TransformationLayout
import com.skydoves.transformationlayout.onTransformationStartContainer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    lateinit var navigation : BottomNavigationView
    private lateinit var transformationLayout: TransformationLayout
    private lateinit var transformationLayoutQuiz: TransformationLayout
    @Inject
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationStartContainer()
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigation = binding.root.findViewById(R.id.bottomNavigationView)
        transformationLayout = binding.transformationLayout
        transformationLayoutQuiz = binding.transformationLayoutQuiz

        val sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val username = sharedPref.getString("username", "User")

        binding.tvUsername.text = "Bok $username"

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
        binding.cvQuiz.setOnClickListener {
         TransformationCompat.startActivity(transformationLayoutQuiz, Intent(this, LecturesListActivity::class.java))
//            startActivity(Intent(this, QuestionsListActivity::class.java))
        }

        Log.d("HomeActivity", "Token: ${sessionManager.authToken}")


//        binding.cvQuiz.setOnClickListener {
//            val navController = findNavController(R.id.nav_graph_image_class)
//            navController.navigate(R.id.tutorialImageClass1Fragment)
//        }

//       binding.cvScanning.setOnClickListener {
//            val navController = findNavController(R.id.fragment_container)
//            navController.navigate(R.id.tutorialCamera1Fragment)
//        }
    }

    override fun onResume() {
        super.onResume()
        navigation.selectedItemId = R.id.home
    }
}