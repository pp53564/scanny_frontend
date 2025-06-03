package com.scanny_project.features.language

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ui_ux_demo.R
import com.example.ui_ux_demo.databinding.ActivitySelectLanguageBinding
import com.scanny_project.data.SessionManager
import com.scanny_project.features.camera.CameraActivity
import com.scanny_project.features.home.HomeActivity
import com.scanny_project.features.home.MainActivity
import com.scanny_project.features.lectures.LectureListActivity
import com.scanny_project.features.teacher.home.TeacherHomeActivity
import com.scanny_project.utils.LanguageAdapter
import com.scanny_project.utils.LanguageData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class SelectLanguageActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_MODE = "EXTRA_MODE"
        const val MODE_CAPTURE = "MODE_CAPTURE"
        const val MODE_LECTURE = "MODE_LECTURE"
    }

    private lateinit var binding: ActivitySelectLanguageBinding
    @Inject lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.titleText.text = getString(R.string.selectLanguage)
        binding.header.buttonBackLayout.imButtonBack.setOnClickListener {
            val homeCls = if (sessionManager.isTeacher)
                TeacherHomeActivity::class.java
            else
                HomeActivity::class.java
            startActivity(Intent(this, homeCls))
        }

        val nextActivity = when (intent.getStringExtra(EXTRA_MODE) ?: MODE_CAPTURE) {
            MODE_LECTURE -> LectureListActivity::class.java
            else         -> CameraActivity::class.java
        }

        binding.recyclerLanguages.layoutManager = GridLayoutManager(this, 2)
        val adapter = LanguageAdapter(LanguageData.languages) { selectedLanguage ->
            Intent(this, nextActivity).also {
                it.putExtra("SELECTED_LANGUAGE", selectedLanguage.code)
                startActivity(it)
            }
        }
        binding.recyclerLanguages.adapter = adapter
    }
}