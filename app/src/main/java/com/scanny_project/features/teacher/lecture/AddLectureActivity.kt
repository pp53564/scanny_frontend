package com.scanny_project.features.teacher.lecture

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ui_ux_demo.R
import com.example.ui_ux_demo.databinding.ActivityAddLectureBinding
import com.google.mlkit.nl.translate.Translator
import com.scanny_project.data.model.ScannedItem
import com.scanny_project.features.camera.CameraActivity
import com.scanny_project.utils.LanguageData
import com.scanny_project.utils.TranslatorHelper
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class AddLectureActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddLectureBinding
    private lateinit var adapter: ScannedItemsAdapter
    private val viewModel: AddLectureViewModel by viewModels()

    private val scanLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val words = result.data?.getStringArrayListExtra("SCANNED_ITEMS").orEmpty()

            val scannedItems = words.map { w ->
                ScannedItem(w, true, mutableMapOf("en" to w))
            }
            viewModel.setItems(scannedItems)
            addTranslations(scannedItems, "en")
        }
    }

    private val editLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult
        val data = result.data ?: return@registerForActivityResult

        val position = data.getIntExtra(EXTRA_POSITION, -1)
        if (position !in 0 until (viewModel.scannedItems.value?.size ?: 0)) return@registerForActivityResult

        val oldList = viewModel.scannedItems.value.orEmpty()
        val oldItem = oldList[position]

        val updatedTranslations = oldItem.translations.toMutableMap().apply {
            this["hr"] = data.getStringExtra(EditTranslationActivity.EXTRA_HR).orEmpty()
            this["en"] = data.getStringExtra(EditTranslationActivity.EXTRA_EN).orEmpty()
            this["de"] = data.getStringExtra(EditTranslationActivity.EXTRA_DE).orEmpty()
            this["fr"] = data.getStringExtra(EditTranslationActivity.EXTRA_FR).orEmpty()
            this["it"] = data.getStringExtra(EditTranslationActivity.EXTRA_IT).orEmpty()
        }
        val newItem = oldItem.copy(translations = updatedTranslations)

        viewModel.updateItem(position, newItem)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLectureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ScannedItemsAdapter(
            onEditClick = { item, pos ->
                val intent = Intent(this, EditTranslationActivity::class.java).apply {
                    putExtra(EditTranslationActivity.EXTRA_HR, item.translations["hr"])
                    putExtra(EditTranslationActivity.EXTRA_EN, item.translations["en"])
                    putExtra(EditTranslationActivity.EXTRA_DE, item.translations["de"])
                    putExtra(EditTranslationActivity.EXTRA_FR, item.translations["fr"])
                    putExtra(EditTranslationActivity.EXTRA_IT, item.translations["it"])
                    putExtra(EXTRA_POSITION, pos)
                }
                editLauncher.launch(intent)
            }
        )

        binding.header.titleText.text = getString(R.string.addLecture)
        binding.header.buttonBackLayout.imButtonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.rvScannedItems.apply {
            layoutManager = LinearLayoutManager(this@AddLectureActivity)
            adapter = this@AddLectureActivity.adapter
        }

        viewModel.scannedItems.observe(this) { list ->
            adapter.setItems(list.toMutableList())
        }

        binding.cvCamera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java).apply {
                putExtra("RETURN_ON_BACK", true)
                putExtra("SELECTED_LANGUAGE", "hr")
            }
            scanLauncher.launch(intent)
        }

        binding.saveLecture.setOnClickListener {
            binding.etTitle.error = null
            val lessonName = binding.etTitle.text.toString().trim()
            val items = viewModel.getSelectedItems()
            viewModel.setLectureName(lessonName)
            viewModel.setSelectedItems(items)
            viewModel.createLecture()
            Log.i("AddLecture", "Sending lesson '$lessonName' with items $items")
        }

        binding.btnAddWord.setOnClickListener {
            val newWord = binding.etNewWord.text.toString().trim().lowercase(Locale.ROOT)
            if (newWord.isNotEmpty()) {
                val scannedItem = ScannedItem(newWord, true, mutableMapOf("hr" to newWord))
                addTranslations(listOf(scannedItem), "hr")
                viewModel.addItem(scannedItem)
                binding.etNewWord.text?.clear()
                binding.rvScannedItems.scrollToPosition(
                    (viewModel.scannedItems.value?.size ?: 1) - 1
                )
            }
        }


        viewModel.uiState.observe(this) { state ->
            when (state) {
                is AddLectureViewModel.UiState.Idle -> {
                    binding.progressBar.isVisible = false
                    binding.saveLecture.isEnabled = true
                    binding.etTitle.error = null
                }
                is AddLectureViewModel.UiState.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.saveLecture.isEnabled = false
                }
                is AddLectureViewModel.UiState.ValidationError -> {
                    binding.progressBar.isVisible = false
                    binding.saveLecture.isEnabled = true
                    binding.etTitle.error = state.nameError
                    state.itemsError?.let { msg ->
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                    }
                }
                is AddLectureViewModel.UiState.Success -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(
                        this,
                        getString(R.string.lecture_created_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                is AddLectureViewModel.UiState.Error -> {
                    binding.progressBar.isVisible = false
                    binding.saveLecture.isEnabled = true
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
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
                                item.translations[langOpt.code] = realText.lowercase(Locale.ROOT)
                                if(code != "hr") {
                                    val pos = adapter.findPosition(item)
                                    if (pos >= 0) viewModel.updateItem(pos, item)
                                }
                            }
                            .addOnFailureListener {}
                    }
                }
            }
    }

    companion object {
        val EXTRA_POSITION: String? = null
    }
}
