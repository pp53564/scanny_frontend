package com.scanny_project.features.stats

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ui_ux_demo.R
import com.example.ui_ux_demo.databinding.ActivityStatsBinding
import com.scanny_project.features.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStatsBinding
    private val viewModel: StatsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.statsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.header.titleText.text = getString(R.string.stats)

        viewModel.loadUserLanguageStats()

        viewModel.languageStatsList.observe(this) { statsList ->
            binding.statsRecyclerView.adapter = StatsAdapter(statsList)
        }

        binding.header.buttonBackLayout.imButtonBack.setOnClickListener {
//            val intent = Intent(this, HomeActivity::class.java)
//            startActivity(intent)
//            finish()
            onBackPressedDispatcher.onBackPressed()
        }
    }

//    private fun updateUI(statsList: List<StatsPerUserAndLanguageDTO>) {
//        binding.statsRecyclerView.layoutManager = LinearLayoutManager(this)
//
//        viewModel.loadUserLanguageStats()
//
//        Log.i("petra4", statsList.toString())
//        viewModel.languageStatsList.value.let { statsList ->
//            binding.statsRecyclerView.adapter = statsList?.let { StatsAdapter(it)}
//        }
//
//    }
}
