package com.scanny_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ui_ux_demo.R
import com.example.ui_ux_demo.databinding.ActivitySelectLanguageBinding
import com.scanny_project.utils.LanguageAdapter

class SelectLanguageActivityForImageClassification : AppCompatActivity() {
    private lateinit var binding: ActivitySelectLanguageBinding
    private val languages = listOf(
        LanguageOption("hr", "Hrvatski", R.drawable.hr),
        LanguageOption("en", "Engleski", R.drawable.uk_flag),
        LanguageOption("it", "Talijanski", R.drawable.italy_flag),
        LanguageOption("de", "Njemački", R.drawable.german_flag),
        LanguageOption("fr", "Francuski", R.drawable.france_flag),
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBackLayout.imButtonBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        binding.recyclerLanguages.layoutManager = GridLayoutManager(this, 2)

        val adapter = LanguageAdapter(languages) { selectedLanguage ->
            val intent = Intent(this, LecturesListActivity::class.java)
            intent.putExtra("SELECTED_LANGUAGE", selectedLanguage.code)
            startActivity(intent)
        }

        binding.recyclerLanguages.adapter = adapter

    }
}