package com.scanny_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ui_ux_demo.R
import com.example.ui_ux_demo.databinding.ActivityImageClassificationAndQuizBinding

class ImageClassificationAndQuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageClassificationAndQuizBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageClassificationAndQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}