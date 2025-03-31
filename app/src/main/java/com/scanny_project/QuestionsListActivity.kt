package com.scanny_project

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ui_ux_demo.databinding.ActivityQuestionsListBinding
import com.scanny_project.data.LectureRepository
import com.scanny_project.data.Result
import com.scanny_project.data.model.UserQuestionDTO
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class QuestionsListActivity : AppCompatActivity() {

    @Inject
    lateinit var lectureRepository: LectureRepository

    private lateinit var binding: ActivityQuestionsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityQuestionsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBackLayout.imButtonBack.setOnClickListener {
            val intent = Intent(this, SelectLanguageActivityForImageClassification::class.java)
            startActivity(intent)
        }

        val lectureId = intent.getLongExtra("LECTURE_ID", -1)
        val selectedLangCode = intent.getStringExtra("SELECTED_LANGUAGE")
        if (lectureId == -1L) {
            finish()
            return
        }

        lifecycleScope.launch {
            val questionsResult = selectedLangCode?.let {
                lectureRepository.getUserQuestionsByLectureAndLang(lectureId,
                    it
                )
            }
            if (questionsResult is Result.Success<List<UserQuestionDTO>>) {
                val questions = questionsResult.data

                val adapter = QuestionsAdapter(questions) { selectedQuestion ->
                    val intent = Intent(this@QuestionsListActivity, ImageClassificationAndQuizActivity::class.java)
                    intent.putExtra("QUESTION_KEYWORD", selectedQuestion.localizedSubject)
                    intent.putExtra("QUESTION_ID", selectedQuestion.id)
                    intent.putExtra("SELECTED_LANGUAGE", selectedLangCode)
                    startActivity(intent)
                }

                binding.questionList.layoutManager = LinearLayoutManager(this@QuestionsListActivity)
                binding.questionList.adapter = adapter

            } else {
                Toast.makeText(this@QuestionsListActivity, "Failed to fetch questions", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
