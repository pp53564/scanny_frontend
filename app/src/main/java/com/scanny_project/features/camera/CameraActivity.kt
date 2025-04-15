package com.scanny_project.features.camera

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ui_ux_demo.R
import com.example.ui_ux_demo.databinding.ActivityCameraBinding
import com.scanny_project.features.home.HomeActivity
import com.scanny_project.features.language.SelectLanguageActivity
import com.scanny_project.utils.LanguageData
import com.scanny_project.utils.TextToSpeechHelper
import com.scanny_project.utils.TranslatorHelper

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBackLayout.imButtonBack.setOnClickListener {
            val intent = Intent(this, SelectLanguageActivity::class.java)
            startActivity(intent)
        }
//        val overlayView = findViewById<OverlayView>(R.id.overlay)
//        TranslatorHelper.initializeTranslator(TranslateLanguage.ITALIAN) { translator ->
//            overlayView.setTranslator(translator)
//        }

         val selectedLangCode = intent.getStringExtra("SELECTED_LANGUAGE")
         binding.tvLanguageTitle.text = LanguageData.languages.firstOrNull { it.code == selectedLangCode }?.name.toString()

        val fragment = CameraFragment().apply {
            arguments = Bundle().apply {
                putString("SELECTED_LANGUAGE", selectedLangCode)
            }
        }

        TranslatorHelper.initializeTranslator(selectedLangCode!!) { translator ->
            lifecycle.addObserver(translator)
//            val overlayView = findViewById<OverlayView>(R.id.overlay)
//            overlayView.setCustomTranslator(translator)
            fragment.setTranslator(translator)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            finishAfterTransition()
        } else {
            super.onBackPressed()
        }
    }
}