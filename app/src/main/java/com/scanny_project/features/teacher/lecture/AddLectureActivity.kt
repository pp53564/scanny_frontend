package com.scanny_project.features.teacher.lecture

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ui_ux_demo.R
import com.example.ui_ux_demo.databinding.ActivityAddLectureBinding
import com.google.mlkit.nl.translate.Translator
import com.scanny_project.data.model.ScannedItem
import com.scanny_project.features.camera.CameraActivity
import com.scanny_project.utils.LanguageData
import com.scanny_project.utils.TranslatorHelper
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class AddLectureActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddLectureBinding
    private lateinit var adapter: ScannedItemsAdapter

    private val scanLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val words = result.data?.getStringArrayListExtra("SCANNED_ITEMS").orEmpty()

            val scannedItems = words.map { w ->
                ScannedItem(w, true, mutableMapOf("en" to w) )
            }
            addTranslations(scannedItems, "en")
            adapter.setItems(scannedItems)

        }
    }

    private val editLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult
        val data = result.data ?: return@registerForActivityResult


        val position = data.getIntExtra(EXTRA_POSITION, -1)
        if (position !in 0 until adapter.itemCount) return@registerForActivityResult

        val item = adapter.items[position].apply {
            translations["hr"] = data.getStringExtra(EditTranslationActivity.EXTRA_HR).orEmpty()
            translations["en"] = data.getStringExtra(EditTranslationActivity.EXTRA_EN).orEmpty()
            translations["de"] = data.getStringExtra(EditTranslationActivity.EXTRA_DE).orEmpty()
            translations["fr"] = data.getStringExtra(EditTranslationActivity.EXTRA_FR).orEmpty()
            translations["it"] = data.getStringExtra(EditTranslationActivity.EXTRA_IT).orEmpty()
        }

        adapter.updateItem(position, item)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLectureBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        preloadModels()

        adapter = ScannedItemsAdapter(
            onEditClick = { item, pos ->
                val intent = Intent(this, EditTranslationActivity::class.java).apply {
                    putExtra(EditTranslationActivity.EXTRA_HR, item.translations["hr"])
                    putExtra(EditTranslationActivity.EXTRA_EN,     item.translations["en"])
                    putExtra(EditTranslationActivity.EXTRA_DE,     item.translations["de"])
                    putExtra(EditTranslationActivity.EXTRA_FR,     item.translations["fr"])
                    putExtra(EditTranslationActivity.EXTRA_IT,     item.translations["it"])

                    putExtra(EXTRA_POSITION, pos)
                }
                editLauncher.launch(intent)
            }
        )

        binding.header.titleText.text = getString(R.string.addLecture)

        binding.header.buttonBackLayout.imButtonBack.setOnClickListener {
//            val intent = Intent(this, Home::class.java)
//            startActivity(intent)
            onBackPressedDispatcher.onBackPressed()
        }

        binding.rvScannedItems.apply {
            layoutManager = LinearLayoutManager(this@AddLectureActivity)
            adapter = this@AddLectureActivity.adapter
        }

        binding.cvCamera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java).apply {
                putExtra("RETURN_ON_BACK", true)
                putExtra("SELECTED_LANGUAGE", "hr")
            }
            scanLauncher.launch(intent)
        }

        binding.saveLecture.setOnClickListener {
            val lessonName = binding.username.text.toString().trim()
            val selected = adapter.getSelectedItems()
            Log.i("AddLecture", "Sending lesson '$lessonName' with items $selected")
        }

        binding.btnAddWord.setOnClickListener {
            val newWord = binding.etNewWord.text.toString().trim()
            val scannedItem = ScannedItem(newWord, true, mutableMapOf("hr" to newWord))
            if (newWord.isNotEmpty()) {
                adapter.addItem(scannedItem)
                binding.etNewWord.text?.clear()
                binding.rvScannedItems.scrollToPosition(adapter.itemCount - 1)
                addTranslations(listOf(scannedItem), "hr")
            }
        }


    }

    private fun addTranslations(scannedItems: List<ScannedItem>, code: String) {
        LanguageData.languages
            .filter { it.code != code }
            .forEach { langOpt ->
                TranslatorHelper.initializeTranslator(langOpt.code, code) { tr ->
                    scannedItems.forEachIndexed { _, item ->
                        tr.translate(item.name)
                            .addOnSuccessListener { realText ->
                                item.translations[langOpt.code] = realText
                                val pos = adapter.findPosition(item)
                                if (pos >= 0) adapter.updateItem(pos, item)
                            }
                            .addOnFailureListener { err ->
//                            item.translations[langCode] = ""
//                            adapter.updateItem(idx, item)
                            }
                    }
                }
            }
    }

    companion object {
        val EXTRA_POSITION: String? = null
    }
}