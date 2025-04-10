package com.scanny_project.features.stats

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ui_ux_demo.R
import com.example.ui_ux_demo.databinding.ActivityStatsDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatsDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStatsDetailsBinding
    private val viewModel: StatsDetailsViewModel by viewModels()
    private lateinit var adapter: StatsDetailsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.titleText.text = getString(R.string.stats_neighbors)

        val languageCode = intent.getStringExtra("LANGUAGE_CODE") ?: ""
        val sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val username = sharedPref.getString("username", "")

        val recycler = findViewById<RecyclerView>(R.id.recyclerStatsDetails)
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = StatsDetailsAdapter(emptyList(), username!!)
        recycler.adapter = adapter

        viewModel.neighborsForLanguageList.observe(this) { stats ->
                adapter.updateData(stats)
        }

        binding.header.buttonBackLayout.imButtonBack.setOnClickListener {
            val intent = Intent(this, StatsActivity::class.java)
            startActivity(intent)
        }

        viewModel.loadNeighborsForLanguage(languageCode)
    }
}
