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

        lifecycleScope.launch {
            val lecturesResult = lectureRepository.getAllUserLectures()
            if (lecturesResult is Result.Success) {
                val lectures = lecturesResult.data
                setupRecyclerView(lectures)
            } else {
                Log.e("LecturesListActivity", "Failed to fetch lectures")
            }
        }
    }

    private fun setupRecyclerView(lectures: List<UserLectureDTO>) {
        val adapter = LecturesAdapter(lectures) { selectedLecture ->
            val intent = Intent(this, QuestionsListActivity::class.java).apply {
                putExtra("LECTURE_ID", selectedLecture.id)
            }
            startActivity(intent)
        }

        binding.lecturesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.lecturesRecyclerView.adapter = adapter
    }
}
