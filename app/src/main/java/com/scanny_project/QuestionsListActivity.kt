package com.scanny_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ui_ux_demo.databinding.ActivityQuestionsListBinding
import com.scanny_project.data.LectureRepository
import com.scanny_project.data.Result
import com.scanny_project.data.model.QuestionDTO
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

        val lectureId = intent.getLongExtra("LECTURE_ID", -1)
        if (lectureId == -1L) {
            // Handle error: no lectureId passed
            finish()
            return
        }

        // Fetch questions for the selected lecture
        lifecycleScope.launch {
            val questionsResult = lectureRepository.getQuestionsByLecture(lectureId)
            if (questionsResult is Result.Success) {
                val questions = questionsResult.data

                val adapter = QuestionsAdapter(questions) { selectedQuestion ->
                    val intent = Intent(this@QuestionsListActivity, ImageClassificationAndQuizActivity::class.java)
                    // Assuming `selectedQuestion.subject` is the keyword or the thing the user should capture.
                    intent.putExtra("QUESTION_KEYWORD", selectedQuestion.subject)
                    startActivity(intent)
                }

                binding.questionList.layoutManager = LinearLayoutManager(this@QuestionsListActivity)
                binding.questionList.adapter = adapter

            } else {
                // Handle error scenario
                Toast.makeText(this@QuestionsListActivity, "Failed to fetch questions", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
