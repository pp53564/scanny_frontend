package com.scanny_project.features.tutorial.tutorialFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ui_ux_demo.databinding.FragmentTutorialCamera1Binding
import android.speech.tts.TextToSpeech
import android.util.Log
import com.scanny_project.utils.TextToSpeechHelper
import java.util.Locale

class TutorialCamera1Fragment : Fragment(){
    private lateinit var binding : FragmentTutorialCamera1Binding
    private lateinit var ttsHelper: TextToSpeechHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTutorialCamera1Binding.inflate(inflater, container, false)
        val root: View = binding.root

        ttsHelper = TextToSpeechHelper(requireContext()) {
            speakTutorialText()
        }

        binding.skipTutorialButton.setOnClickListener {
            findNavController().navigate(
                TutorialCamera1FragmentDirections.actionTutorialCamera1FragmentToHomeActivity()
            )
            ttsHelper.shutdown()
        }
        binding.btnTutorialNext.setOnClickListener {
            findNavController().navigate(
                TutorialCamera1FragmentDirections.actionTutorialCamera1FragmentToTutorialCamera2Fragment()
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