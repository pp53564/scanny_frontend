package com.scanny_project.features.tutorial.tutorialFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ui_ux_demo.R
import com.example.ui_ux_demo.databinding.FragmentTutorialImageClass1Binding

class TutorialImageClass1Fragment : Fragment() {
    private lateinit var binding : FragmentTutorialImageClass1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTutorialImageClass1Binding.inflate(inflater, container, false)
        val root: View = binding.root
        val navController = findNavController()

//        binding.skipTutorialbButton.setOnClickListener {
//            navController.navigate(R.id.action_tutorialImageClass1Fragment_to_imageClassificationAndQuizActivity)
//        }
//
//        binding.btnTutorialNext.setOnClickListener {
//            navController.navigate(R.id.action_tutorialImageClass1Fragment_to_imageClassificationAndQuizActivity)
//        }

        return root
    }
}