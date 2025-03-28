package com.scanny_project.TutorialFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ui_ux_demo.databinding.FragmentTutorialCamera4Binding

class TutorialCamera4Fragment : Fragment() {

    private lateinit var binding : FragmentTutorialCamera4Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTutorialCamera4Binding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.btnTutorialNext.setOnClickListener {
            findNavController().navigate(
                TutorialCamera4FragmentDirections.actionTutorialCamera4FragmentToHomeActivity()
            )
        }
        binding.btnTutorialBack.setOnClickListener {
            findNavController().navigate(
                TutorialCamera4FragmentDirections.actionTutorialCamera4FragmentToTutorialCamera3Fragment()
            )
        }
        return root
    }

}