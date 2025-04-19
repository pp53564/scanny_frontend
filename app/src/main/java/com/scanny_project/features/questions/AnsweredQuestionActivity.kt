package com.scanny_project.features.questions

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.ui_ux_demo.R
import com.example.ui_ux_demo.databinding.ActivityAnsweredQuestionBinding
import com.example.ui_ux_demo.databinding.ActivityQuestionsListBinding
import com.scanny_project.features.language.SelectLanguageActivityForImageClassification
import com.scanny_project.utils.TextToSpeechHelper
import dagger.hilt.android.AndroidEntryPoint
import java.util.Base64
@AndroidEntryPoint
class AnsweredQuestionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnsweredQuestionBinding
    private val viewModel: AnsweredQuestionViewModel by viewModels()
    private lateinit var ttsHelper: TextToSpeechHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnsweredQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val questionId   = intent.getLongExtra("QUESTION_ID", 0L)
        val selectedLangCode  = intent.getStringExtra("SELECTED_LANGUAGE")!!

        viewModel.load(questionId, selectedLangCode)

        ttsHelper = TextToSpeechHelper(this, languageCode = selectedLangCode)

        binding.buttonBackLayout.imButtonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        viewModel.question.observe(this) { dto ->
            binding.ivSpeaker.setOnClickListener {
                ttsHelper.speak(dto.text)
            }

            val bytes  = Base64.getDecoder().decode(dto.imageBase64)
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            binding.ivAnswerImage.setImageBitmap(bitmap)
            binding.tvAnswerText.text = dto.text
        }

//        viewModel.error.observe(this) {
//            it?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
//        }

        viewModel.errorMessage.observe(this) { error ->
            if (!error.isNullOrEmpty()) {
                Log.e("QuestionListActivity", error)
//                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        ttsHelper.shutdown()
        super.onDestroy()
    }
}