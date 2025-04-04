package com.scanny_project.features.tutorial.tutorialFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ui_ux_demo.databinding.FragmentTutorialCamera2Binding


class TutorialCamera2Fragment : Fragment() {

    private lateinit var binding : FragmentTutorialCamera2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTutorialCamera2Binding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.skipTutorialButton.setOnClickListener {
            findNavController().navigate(
                TutorialCamera2FragmentDirections.actionTutorialCamera2FragmentToHomeActivity()
            )
        }
        binding.btnTutorialNext.setOnClickListener {
            findNavController().navigate(
                TutorialCamera2FragmentDirections.actionTutorialCamera2FragmentToTutorialCamera3Fragment()
            )
        }
        binding.btnTutorialBack.setOnClickListener {
            findNavController().navigate(
                TutorialCamera2FragmentDirections.actionTutorialCamera2FragmentToTutorialCamera1Fragment()
            )
        }
        return root
    }

}