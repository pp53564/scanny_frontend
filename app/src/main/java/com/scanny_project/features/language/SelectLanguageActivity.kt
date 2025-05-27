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
import com.scanny_project.features.teacher.home.TeacherHomeActivity
import com.scanny_project.utils.LanguageAdapter
import com.scanny_project.utils.LanguageData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SelectLanguageActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectLanguageBinding
    @Inject
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.titleText.text = getString(R.string.selectLanguage)

        binding.header.buttonBackLayout.imButtonBack.setOnClickListener {
            val homeCls = if (sessionManager.userRole == "ROLE_TEACHER")
                TeacherHomeActivity::class.java
            else
                MainActivity::class.java
            startActivity(Intent(this, homeCls))
//            finish()
        }

        binding.recyclerLanguages.layoutManager = GridLayoutManager(this, 2)

        val adapter = LanguageAdapter(
            LanguageData.languages
        ) { selectedLanguage ->
            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra("SELECTED_LANGUAGE", selectedLanguage.code)
            startActivity(intent)
        }
        binding.recyclerLanguages.adapter = adapter

    }
}