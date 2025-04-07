package com.scanny_project.features.lectures

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ui_ux_demo.R
import com.example.ui_ux_demo.databinding.ActivityLecturesListBinding
import com.scanny_project.features.language.SelectLanguageActivityForImageClassification
import com.scanny_project.data.model.UserLectureDTO
import com.scanny_project.features.questions.QuestionListActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LectureListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLecturesListBinding

    private val viewModel: LectureListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLecturesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.titleText.text = getString(R.string.lessons)

        val selectedLangCode = intent.getStringExtra("SELECTED_LANGUAGE").toString()

        binding.header.buttonBackLayout.imButtonBack.setOnClickListener {
            val intent = Intent(this, SelectLanguageActivityForImageClassification::class.java)
            startActivity(intent)
        }

        viewModel.lectures.observe(this) { lectures ->
            if (lectures != null) {
                setupRecyclerView(lectures, selectedLangCode)
            }
        }

        viewModel.errorMessage.observe(this) { error ->
            if (!error.isNullOrEmpty()) {
                Log.e("LecturesListActivity", error)
            }
        }

        viewModel.loadLectures(selectedLangCode)
    }
    private fun setupRecyclerView(lectures: List<UserLectureDTO>, selectedLangCode: String) {
        val adapter = LecturesAdapter(lectures) { selectedLecture ->
            val intent = Intent(this, QuestionListActivity::class.java).apply {
                putExtra("LECTURE_ID", selectedLecture.id)
                putExtra("SELECTED_LANGUAGE", selectedLangCode)
            }
            startActivity(intent)
        }

        binding.lecturesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.lecturesRecyclerView.adapter = adapter
    }
}
