package com.scanny_project.features.tutorial.tutorialFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ui_ux_demo.databinding.FragmentTutorialCamera2Binding
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class TutorialCamera2Fragment : Fragment() {

    private lateinit var binding : FragmentTutorialCamera2Binding
    private var textToSpeech: TextToSpeech? = null

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
        textToSpeech = TextToSpeech(requireContext()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale.forLanguageTag("hr")
                textToSpeech?.setPitch(1.2f)
                textToSpeech?.setSpeechRate(1.2f)
                speakTutorialText()
            } else {
                Log.e("TTS", "Initialization failed")
            }
        }
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
    private fun speakTutorialText() {
        val message = binding.tvSpeech.text.toString()
        textToSpeech?.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        textToSpeech?.stop()
        textToSpeech?.shutdown()
    }

}