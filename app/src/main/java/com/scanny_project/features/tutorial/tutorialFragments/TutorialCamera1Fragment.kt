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
import java.util.Locale

class TutorialCamera1Fragment : Fragment(){
    private lateinit var binding : FragmentTutorialCamera1Binding
    private var textToSpeech: TextToSpeech? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTutorialCamera1Binding.inflate(inflater, container, false)
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
                TutorialCamera1FragmentDirections.actionTutorialCamera1FragmentToHomeActivity()
            )
        }
        binding.btnTutorialNext.setOnClickListener {
            findNavController().navigate(
                TutorialCamera1FragmentDirections.actionTutorialCamera1FragmentToTutorialCamera2Fragment()
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