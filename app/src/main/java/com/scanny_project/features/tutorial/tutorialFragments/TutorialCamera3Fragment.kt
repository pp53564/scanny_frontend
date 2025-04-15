package com.scanny_project.features.tutorial.tutorialFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ui_ux_demo.databinding.FragmentTutorialCamera3Binding
import com.scanny_project.utils.TextToSpeechHelper

class TutorialCamera3Fragment : Fragment() {
    private lateinit var binding : FragmentTutorialCamera3Binding
    private lateinit var ttsHelper: TextToSpeechHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTutorialCamera3Binding.inflate(inflater, container, false)
        val root: View = binding.root

        ttsHelper = TextToSpeechHelper(requireContext()) {
            speakTutorialText()
        }

        binding.btnTutorialNext.setOnClickListener {
            findNavController().navigate(
               TutorialCamera3FragmentDirections.actionTutorialCamera3FragmentToTutorialCamera4Fragment()
            )
        }
        binding.btnTutorialBack.setOnClickListener {
            findNavController().navigate(
                TutorialCamera3FragmentDirections.actionTutorialCamera3FragmentToTutorialCamera2Fragment()
            )
            ttsHelper.shutdown()
        }

        binding.skipTutorialButton.setOnClickListener {
            findNavController().navigate(
                TutorialCamera3FragmentDirections.actionTutorialCamera3FragmentToHomeActivity()
            )
            ttsHelper.shutdown()
        }
        return root
    }

    private fun speakTutorialText() {
        val message = binding.tvSpeech.text.toString()
        ttsHelper.speak(message)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ttsHelper.shutdown()
    }

}