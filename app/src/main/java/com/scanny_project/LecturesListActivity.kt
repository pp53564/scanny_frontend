package com.scanny_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ui_ux_demo.databinding.ActivityLecturesListBinding
import com.scanny_project.data.LectureRepository
import com.scanny_project.data.Result
import com.scanny_project.data.model.UserLectureDTO
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LecturesListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLecturesListBinding

    @Inject
    lateinit var lectureRepository: LectureRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLecturesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectedLangCode = intent.getStringExtra("SELECTED_LANGUAGE").toString()

        binding.buttonBackLayout.imButtonBack.setOnClickListener {
            val intent = Intent(this, SelectLanguageActivityForImageClassification::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
            val lecturesResult = lectureRepository.getAllUserLanguageLectures(selectedLangCode)
            if (lecturesResult is Result.Success<List<UserLectureDTO>>) {
                val lectures = lecturesResult.data
                setupRecyclerView(lectures, selectedLangCode)
            } else {
                Log.e("LecturesListActivity", "Failed to fetch lectures")
            }
        }
    }

    private fun setupRecyclerView(lectures: List<UserLectureDTO>, selectedLangCode: String) {
        val adapter = LecturesAdapter(lectures, selectedLangCode) { selectedLecture ->
            val intent = Intent(this, QuestionsListActivity::class.java).apply {
                putExtra("LECTURE_ID", selectedLecture.id)
                putExtra("SELECTED_LANGUAGE", selectedLangCode)
            }
            startActivity(intent)
        }

        binding.lecturesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.lecturesRecyclerView.adapter = adapter
    }
}
