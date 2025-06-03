package com.scanny_project.features.questions

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ui_ux_demo.R
import com.example.ui_ux_demo.databinding.ActivityQuestionsListBinding
import com.scanny_project.data.model.UserQuestionDTO
import com.scanny_project.features.quiz.ImageClassificationAndQuizActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestionListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuestionsListBinding

    private val viewModel: QuestionListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityQuestionsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.buttonBackLayout.imButtonBack.setOnClickListener {
//            val intent = Intent(this, SelectLanguageActivityForImageClassification::class.java)
//            startActivity(intent)
            onBackPressedDispatcher.onBackPressed()
        }

        binding.header.titleText.text = getString(R.string.questions)

        val lectureId = intent.getLongExtra("LECTURE_ID", -1)
        val selectedLangCode = intent.getStringExtra("SELECTED_LANGUAGE")
        if (lectureId == -1L) {
            finish()
            return
        }

        viewModel.questions.observe(this) { questions ->
            if (questions != null) {
                setupRecyclerView(questions, selectedLangCode!!)
            }
        }

        viewModel.errorMessage.observe(this) { error ->
            if (!error.isNullOrEmpty()) {
                Log.e("QuestionListActivity", error)
            }
        }

        viewModel.loadQuestions(lectureId, selectedLangCode!!)
    }

    private fun setupRecyclerView(questions: List<UserQuestionDTO>, selectedLangCode: String) {
        val adapter = QuestionAdapter(questions, selectedLangCode) { selectedQuestion ->
            val intent = Intent(this@QuestionListActivity, ImageClassificationAndQuizActivity::class.java)
            intent.putExtra("QUESTION_KEYWORD", selectedQuestion.localizedSubject)
            intent.putExtra("QUESTION_ID", selectedQuestion.id)
            intent.putExtra("SELECTED_LANGUAGE", selectedLangCode)
            startActivity(intent)
        }

        binding.questionList.layoutManager = LinearLayoutManager(this@QuestionListActivity)
        binding.questionList.adapter = adapter
    }
}
