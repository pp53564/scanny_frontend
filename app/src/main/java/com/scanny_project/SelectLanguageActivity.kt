package com.scanny_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ui_ux_demo.R
import com.example.ui_ux_demo.databinding.ActivitySelectLanguageBinding
import com.scanny_project.utils.LanguageAdapter

data class LanguageOption(val code: String, val name: String, val flagResId: Int)

class SelectLanguageActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectLanguageBinding
    private val languages = listOf(
        LanguageOption("it", "Talijanski", R.drawable.italy_flag),
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerLanguages.layoutManager = LinearLayoutManager(this)

        val adapter = LanguageAdapter(languages) { selectedLanguage ->
            // On language click, go to CameraActivity
            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra("SELECTED_LANGUAGE", selectedLanguage.code)
            startActivity(intent)
        }
        binding.recyclerLanguages.adapter = adapter
    }
}