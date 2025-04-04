package com.scanny_project.features.language

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ui_ux_demo.R
import com.example.ui_ux_demo.databinding.ActivitySelectLanguageBinding
import com.scanny_project.features.camera.CameraActivity
import com.scanny_project.features.home.HomeActivity
import com.scanny_project.utils.LanguageAdapter
import com.scanny_project.utils.LanguageData

class SelectLanguageActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectLanguageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.titleText.text = getString(R.string.selectLanguage)

        binding.header.buttonBackLayout.imButtonBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
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