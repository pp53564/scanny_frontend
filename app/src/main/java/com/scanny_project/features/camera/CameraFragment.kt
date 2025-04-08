package com.scanny_project.features.camera

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.fragment.app.Fragment
import com.example.ui_ux_demo.databinding.FragmentCameraBinding
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.ui_ux_demo.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.mlkit.nl.translate.Translator
import com.scanny_project.data.model.CustomDetection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.vision.detector.Detection
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.speech.tts.TextToSpeech
import com.scanny_project.utils.TextToSpeechHelper
import java.util.Locale

class CameraFragment: Fragment(), ObjectDetectorHelper.DetectorListener{
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private val tag = "ObjectDetection"
    private var _fragmentCameraBinding: FragmentCameraBinding? = null
    private val fragmentCameraBinding
       get() = _fragmentCameraBinding!!

    private lateinit var objectDetectorHelper: ObjectDetectorHelper
    private lateinit var bitmapBuffer: Bitmap
    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var translator: Translator? = null
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var ttsHelper: TextToSpeechHelper
    private var selectedLangCode: String? = null

    private val lastSpokenTimeMap = mutableMapOf<String, Long>()


//    override fun onResume() {
//        super.onResume()
////        if (!PermissionsFragment.hasPermissions(requireContext())) {
////            Navigation.findNavController(requireActivity(), R.id.fragment_container)
////                .navigate(CameraFragmentDirections.actionCameraToPermissions())
////        }
//    }

    override fun onDestroyView() {
        _fragmentCameraBinding = null
        super.onDestroyView()
        ttsHelper.shutdown()
        cameraExecutor.shutdown()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentCameraBinding = FragmentCameraBinding.inflate(inflater, container, false)
        bottomSheetBehavior = BottomSheetBehavior.from(fragmentCameraBinding.bottomSheetLayout.root)

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        fragmentCameraBinding.bottomSheetLayout.imUp.setImageResource(R.drawable.ic_arrow_down_bubble)
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        fragmentCameraBinding.bottomSheetLayout.imUp.setImageResource(R.drawable.ic_arrow_up_bubble)
                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })


        return fragmentCameraBinding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedLangCode = arguments?.getString("SELECTED_LANGUAGE")

        objectDetectorHelper = ObjectDetectorHelper(
            context = requireContext(),
            objectDetectorListener = this
        )
        cameraExecutor = Executors.newSingleThreadExecutor()

        fragmentCameraBinding.viewFinder.post {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    setUpCamera()
            }
        }
        initBottomSheetControls()
    }

//    private fun initTextToSpeech() {
////        textToSpeech = TextToSpeech(requireContext()) { status ->
////            if (status == TextToSpeech.SUCCESS) {
////                textToSpeech?.language = Locale.forLanguageTag(selectedLangCode)
////                textToSpeech?.setPitch(1.2f)
////                textToSpeech?.setSpeechRate(1.2f)
////            } else {
////                Log.e(tag, "TextToSpeech initialization failed")
////            }
////        }
//        ttsHelper = TextToSpeechHelper(requireContext(), languageCode = selectedLangCode) {
//            speakTutorialText()
//        }
//    }

    private fun initBottomSheetControls() {
        // When clicked, lower detection score threshold floor
        /*fragmentCameraBinding.bottomSheetLayout.thresholdMinus.setOnClickListener {
            if (objectDetectorHelper.threshold >= 0.1) {
                objectDetectorHelper.threshold -= 0.1f
                updateControlsUi()
            }
        }

        // When clicked, raise detection score threshold floor
        fragmentCameraBinding.bottomSheetLayout.thresholdPlus.setOnClickListener {
            if (objectDetectorHelper.threshold <= 0.8) {
                objectDetectorHelper.threshold += 0.1f
                updateControlsUi()
            }
        }*/

        // When clicked, reduce the number of objects that can be detected at a time
        fragmentCameraBinding.bottomSheetLayout.maxResultsMinus.setOnClickListener {
            if (objectDetectorHelper.maxResults > 1) {
                objectDetectorHelper.maxResults--
                updateControlsUi()
            }
        }

        // When clicked, increase the number of objects that can be detected at a time
        fragmentCameraBinding.bottomSheetLayout.maxResultsPlus.setOnClickListener {
            if (objectDetectorHelper.maxResults < 3) {
                objectDetectorHelper.maxResults++
                updateControlsUi()
            }
        }

        // When clicked, decrease the number of threads used for detection
      /*  fragmentCameraBinding.bottomSheetLayout.threadsMinus.setOnClickListener {
            if (objectDetectorHelper.numThreads > 1) {
                objectDetectorHelper.numThreads--
                updateControlsUi()
            }
        }

        // When clicked, increase the number of threads used for detection
        fragmentCameraBinding.bottomSheetLayout.threadsPlus.setOnClickListener {
            if (objectDetectorHelper.numThreads < 4) {
                objectDetectorHelper.numThreads++
                updateControlsUi()
            }
        }

        // When clicked, change the underlying hardware used for inference. Current options are CPU
        // GPU, and NNAPI
        fragmentCameraBinding.bottomSheetLayout.spinnerDelegate.setSelection(0, false)
        fragmentCameraBinding.bottomSheetLayout.spinnerDelegate.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    objectDetectorHelper.currentDelegate = p2
                    updateControlsUi()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

        // When clicked, change the underlying model used for object detection
        fragmentCameraBinding.bottomSheetLayout.spinnerModel.setSelection(0, false)
        fragmentCameraBinding.bottomSheetLayout.spinnerModel.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    objectDetectorHelper.currentModel = p2
                    updateControlsUi()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }*/
    }


    private fun updateControlsUi() {
        fragmentCameraBinding.bottomSheetLayout.maxResultsValue.text = objectDetectorHelper.maxResults.toString()

        // Needs to be cleared instead of reinitialized because the GPU
        // delegate needs to be initialized on the thread using it when applicable
        objectDetectorHelper.clearObjectDetector()
        fragmentCameraBinding.overlay.clear()
    }


    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(
            {
                // CameraProvider
                cameraProvider = cameraProviderFuture.get()

                // Build and bind the camera use cases
                bindCameraUseCases()
            },
            ContextCompat.getMainExecutor(requireContext())
        )
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun bindCameraUseCases() {
        val cameraProvider =
            cameraProvider ?: throw IllegalStateException("Camera initialization failed.")
        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

        preview =
            Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(fragmentCameraBinding.viewFinder.display.rotation)
                .build()

        imageAnalyzer =
            ImageAnalysis.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(fragmentCameraBinding.viewFinder.display.rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor) { image ->
                        if (!::bitmapBuffer.isInitialized) {
                            bitmapBuffer = Bitmap.createBitmap(
                                image.width,
                                image.height,
                                Bitmap.Config.ARGB_8888
                            )
                        }
                        detectObjects(image)
                    }
                }
        cameraProvider.unbindAll()

        try {
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)
            preview?.surfaceProvider = fragmentCameraBinding.viewFinder.surfaceProvider
        } catch (exc: Exception) {
            Log.e(tag, "Use case binding failed", exc)
        }
    }


    private fun detectObjects(image: ImageProxy) {
        image.use { bitmapBuffer.copyPixelsFromBuffer(image.planes[0].buffer) }
        val imageRotation = image.imageInfo.rotationDegrees
        objectDetectorHelper.detect(bitmapBuffer, imageRotation)
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        imageAnalyzer?.targetRotation = fragmentCameraBinding.viewFinder.display.rotation
    }

//    override fun onResults(
//        detections: MutableList<Detection>?,
//        inferenceTime: Long,
//        imageHeight: Int,
//        imageWidth: Int
//    ) {
//        if(translator == null) {
//            return
//        }
//
//        activity?.runOnUiThread {
//            val rawDetections = detections ?: return@runOnUiThread
//
//            val translatedDetections = mutableListOf<CustomDetection>()
//            var pendingTranslations = rawDetections.size
//
//            Log.i("italian", pendingTranslations.toString())
//
//            for (det in rawDetections) {
//                val boundingBox = det.boundingBox
//                val score = det.categories[0].score
//                val label = det.categories[0].label
//
//                Log.i("italian", label)
//
//                if(label.toString() != lastLabel || System.currentTimeMillis() - lastSpeakTime > 10000) {
//                    lastLabel = label
//
//                    translator?.translate(label)
//                        ?.addOnSuccessListener { translated ->
//                            val rectF = RectF(
//                                boundingBox.left,
//                                boundingBox.top,
//                                boundingBox.right,
//                                boundingBox.bottom
//                            )
//                            translatedDetections.add(
//                                CustomDetection(rectF, translated, score)
//                            )
//
//                            textToSpeech?.speak(translated, TextToSpeech.QUEUE_ADD, null, null)
//                            lastSpokenLabel = translated
//                            lastSpeakTime = System.currentTimeMillis()
//                            Log.i("italian", translated + label)
//
//                            pendingTranslations--
//                            if (pendingTranslations == 0) {
//                                fragmentCameraBinding.overlay.setTranslatedResults(
//                                    translatedDetections,
//                                    imageHeight,
//                                    imageWidth
//                                )
//                            }
//                        }?.addOnFailureListener {
////
//                        }
//                } else {
//                    pendingTranslations--
//                }
//            }
//        }
//    }

    override fun onResults(
        detections: MutableList<Detection>?,
        inferenceTime: Long,
        imageHeight: Int,
        imageWidth: Int
    ) {
        if (translator == null || detections == null) return

        viewLifecycleOwner.lifecycleScope.launch {
            val groupedDetections = detections.groupBy { it.categories[0].label }
            val translatedDetections = mutableListOf<CustomDetection>()
            var pendingTranslations = groupedDetections.size

            groupedDetections.forEach { (label, detList) ->
                val currentTime = System.currentTimeMillis()
                val lastTime = lastSpokenTimeMap[label] ?: 0L
                val debounceThreshold = 5000L

                if (currentTime - lastTime > debounceThreshold) {
                    lastSpokenTimeMap[label] = currentTime

                    translator?.translate(label)
                        ?.addOnSuccessListener { translated ->
                            detList.forEach { det ->
                                val boundingBox = det.boundingBox
                                val score = det.categories[0].score
                                val rectF = RectF(
                                    boundingBox.left,
                                    boundingBox.top,
                                    boundingBox.right,
                                    boundingBox.bottom
                                )
                                translatedDetections.add(CustomDetection(rectF, translated, score))
                            }
//                            textToSpeech?.speak(translated, TextToSpeech.QUEUE_ADD, null, null)
                            ttsHelper = selectedLangCode?.let {
                                TextToSpeechHelper(requireContext(), languageCode = it) {
                                    ttsHelper.speak(translated)
                                }
                            }!!
                            Log.i("TTS", "Speaking: $translated for label: $label")

                            pendingTranslations--
                            if (pendingTranslations == 0) {
                                fragmentCameraBinding.overlay.setTranslatedResults(
                                    translatedDetections,
                                    imageHeight,
                                    imageWidth
                                )
                            }
                        }?.addOnFailureListener {
                        }
                } else {
                    pendingTranslations--
                }
            }
        }
    }


    public fun setTranslator(translator: Translator) {
        this.translator = translator
    }
    override fun onError(error: String) {
        activity?.runOnUiThread {
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }

}