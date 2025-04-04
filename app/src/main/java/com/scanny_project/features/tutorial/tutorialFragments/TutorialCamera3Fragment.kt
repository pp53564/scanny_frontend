package com.scanny_project.features.tutorial.tutorialFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ui_ux_demo.databinding.FragmentTutorialCamera3Binding

class TutorialCamera3Fragment : Fragment() {

    private lateinit var binding : FragmentTutorialCamera3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTutorialCamera3Binding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.btnTutorialNext.setOnClickListener {
            findNavController().navigate(
               TutorialCamera3FragmentDirections.actionTutorialCamera3FragmentToTutorialCamera4Fragment()
            )
        }
        binding.btnTutorialBack.setOnClickListener {
            findNavController().navigate(
                TutorialCamera3FragmentDirections.actionTutorialCamera3FragmentToTutorialCamera2Fragment()
            )
        }

        binding.skipTutorialButton.setOnClickListener {
            findNavController().navigate(
                TutorialCamera2FragmentDirections.actionTutorialCamera2FragmentToHomeActivity()
            )
        }
        return root
    }

}