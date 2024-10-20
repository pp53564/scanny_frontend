package com.scanny_project.CameraImageClass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ui_ux_demo.databinding.FragmentCameraImageClassBinding

class CameraImageClassFragment: Fragment() {
    private var _fragmentCameraImageClassBinding: FragmentCameraImageClassBinding? = null
    private val fragmentCameraImageClassBinding
        get() = _fragmentCameraImageClassBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentCameraImageClassBinding = FragmentCameraImageClassBinding.inflate(inflater, container, false)

        return fragmentCameraImageClassBinding.root
    }
}