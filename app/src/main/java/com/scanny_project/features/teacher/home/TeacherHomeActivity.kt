package com.scanny_project.features.teacher.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ui_ux_demo.R
import com.example.ui_ux_demo.databinding.ActivityTeacherHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.scanny_project.data.SessionManager
import com.scanny_project.features.language.SelectLanguageActivity
import com.scanny_project.features.profile.ProfileActivity
import com.scanny_project.features.stats.StatsActivity
import com.scanny_project.features.teacher.lecture.AddLectureActivity
import com.scanny_project.utils.setupNavigation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TeacherHomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTeacherHomeBinding
    @Inject
    lateinit var sessionManager: SessionManager
    lateinit var navigation : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigation = binding.root.findViewById(R.id.bottomNavigationView)

        navigation.setupNavigation(
            mapOf(
                R.id.home to { true },
                R.id.camera to {
                    Intent(this, SelectLanguageActivity::class.java).also {
                        it.putExtra(SelectLanguageActivity.EXTRA_MODE,
                            SelectLanguageActivity.MODE_CAPTURE
                        )
                        startActivity(it)
                    } },
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
            Intent(this, SelectLanguageActivity::class.java).also {
                it.putExtra(SelectLanguageActivity.EXTRA_MODE, SelectLanguageActivity.MODE_LECTURE)
                startActivity(it)
            }
        }

        binding.cvAddLection.setOnClickListener {
//            val navController = findNavController(R.id.fragment_container_tutorial)
//            navController.navigate(R.id.tutorialCamera1Fragment)
                startActivity(Intent(this, AddLectureActivity::class.java))
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