package com.scanny_project.features.camera

import android.app.Activity
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

    private val returnOnBack: Boolean
        get() = intent.getBooleanExtra("RETURN_ON_BACK", false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

         binding.buttonBackLayout.imButtonBack.setOnClickListener {
             onBackPressed()
         }
//            if (returnOnBack) {
//                val camFrag = supportFragmentManager
//                    .findFragmentById(R.id.fragment_container) as? CameraFragment
//                val items = camFrag?.getScannedItems().orEmpty()
//
//                Log.i("petra1", items[0]);
//
//                Intent().apply {
//                    putStringArrayListExtra("SCANNED_ITEMS", ArrayList(items))
//                }.also { data ->
//                    setResult(Activity.RESULT_OK, data)
//                }
//                onBackPressedDispatcher.onBackPressed()
//            } else {
//                val intent = Intent(this, SelectLanguageActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//
////            onBackPressedDispatcher.onBackPressed()
//        }
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

        TranslatorHelper.initializeTranslator(selectedLangCode!!, "en") { translator ->
            lifecycle.addObserver(translator)
//            val overlayView = findViewById<OverlayView>(R.id.overlay)
//            overlayView.setCustomTranslator(translator)
            fragment.setTranslator(translator)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onBackPressed() {
        if (returnOnBack) {
            val camFrag = supportFragmentManager
                .findFragmentById(R.id.fragment_container) as? CameraFragment
            val items = camFrag?.getScannedItems().orEmpty()

            val data = Intent().apply {
                putStringArrayListExtra("SCANNED_ITEMS", ArrayList(items))
            }
            setResult(Activity.RESULT_OK, data)
            onBackPressedDispatcher.onBackPressed()
            finish()
        } else {
//            val intent = Intent(this, SelectLanguageActivity::class.java)
//                startActivity(intent)
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                finishAfterTransition()
            } else {
                finish()
            }
        }
        super.onBackPressed()
    }

}