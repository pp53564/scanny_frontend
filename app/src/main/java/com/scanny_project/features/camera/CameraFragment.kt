package com.scanny_project.features.camera

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
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
//    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
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
    private var lastLabel: String = ""
    private var lastSpokenTime: Long = 0L

    private val scannedLabels = mutableSetOf<String>()

    private val colorList by lazy {
        listOf(
            ContextCompat.getColor(requireContext(), R.color.pink),
            ContextCompat.getColor(requireContext(), R.color.button_start),
            ContextCompat.getColor(requireContext(), R.color.green_strong)
        )
    }
    private var colorIndex = 0


//    override fun onResume() {
//        super.onResume()
////        if (!PermissionsFragment.hasPermissions(requireContext())) {
////            Navigation.findNavController(requireActivity(), R.id.fragment_container)
////                .navigate(CameraFragmentDirections.actionCameraToPermissions())
////        }
//    }

//    override fun onDestroyView() {
//        _fragmentCameraBinding = null
//        super.onDestroyView()
//        ttsHelper.shutdown()
//        cameraExecutor.shutdown()
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentCameraBinding = FragmentCameraBinding.inflate(inflater, container, false)

//        bottomSheetBehavior = BottomSheetBehavior.from(fragmentCameraBinding.bottomSheetLayout.root)
//
//        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                when (newState) {
//                    BottomSheetBehavior.STATE_EXPANDED -> {
//                        fragmentCameraBinding.bottomSheetLayout.imUp.setImageResource(R.drawable.ic_arrow_down_bubble)
//                    }
//                    BottomSheetBehavior.STATE_COLLAPSED -> {
//                        fragmentCameraBinding.bottomSheetLayout.imUp.setImageResource(R.drawable.ic_arrow_up_bubble)
//                    }
//                }
//            }
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//            }
//        })


        return fragmentCameraBinding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedLangCode = arguments?.getString("SELECTED_LANGUAGE")
        if(selectedLangCode.isNullOrBlank()) selectedLangCode = "hr"
        ttsHelper = selectedLangCode?.let { TextToSpeechHelper(requireContext(), languageCode = it) }!!

        objectDetectorHelper = ObjectDetectorHelper(
            context = requireContext(),
            objectDetectorListener = this
        ).apply {
            maxResults = 1
        }
        cameraExecutor = Executors.newSingleThreadExecutor()

        fragmentCameraBinding.viewFinder.post {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    setUpCamera()
            }
        }
    }

    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(
            {
                cameraProvider = cameraProviderFuture.get()

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
        if (ttsHelper.isSpeaking) {
            image.close()
            return
        }

        image.use {
            bitmapBuffer.copyPixelsFromBuffer(image.planes[0].buffer)
        }
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

        lifecycleScope.launch {
            val groupedDetections = detections.groupBy { it.categories[0].label }
            val translatedDetections = mutableListOf<CustomDetection>()
            var pendingTranslations = groupedDetections.size
            var score: Float? = null

            groupedDetections.forEach { (label, detList) ->
                val currentTime = System.currentTimeMillis()
                val normalizedLabel = label.trim().lowercase()
                val debounceThreshold = 7000L

                val isDifferent = normalizedLabel != lastLabel
                val isPastDebounce = currentTime - lastSpokenTime > debounceThreshold

                if (isDifferent || isPastDebounce) {
                    lastSpokenTime = currentTime
                    if (isDifferent) {
                        colorIndex = (colorIndex + 1) % colorList.size
                        lastLabel = normalizedLabel
                    }

                    if(selectedLangCode != "en") {
                        translator?.translate(label)
                            ?.addOnSuccessListener { translated ->
                                if(!scannedLabels.contains(translated)) scannedLabels.add(label.lowercase(Locale.ROOT))

                                val translatedNormalized = translated.trim().lowercase()
                                val labelNormalized = label.trim().lowercase()
                                // ako ne zna prevest da preskocis ->isprobaj
                                if (translatedNormalized == labelNormalized) {
                                    Log.w("Translation", "No translation found for '$label'")
                                    return@addOnSuccessListener
                                }
                                detList.forEach { det ->
                                    val boundingBox = det.boundingBox
                                    val boxWidth = boundingBox.width()

                                    val minSizePx = 200f

                                    if (boxWidth < minSizePx) {
                                        return@addOnSuccessListener
                                    }
//                                    val boundingBox = det.boundingBox
                                    score = det.categories[0].score
                                    Log.i("score", score.toString())

                                    val rectF = RectF(
                                        boundingBox.left,
                                        boundingBox.top,
                                        boundingBox.right,
                                        boundingBox.bottom
                                    )
                                    translatedDetections.add(
                                        CustomDetection(
                                            rectF,
                                            translated,
                                            score!!,
                                            colorList[colorIndex]
                                        )
                                    )

                                }

//                            textToSpeech?.speak(translated, TextToSpeech.QUEUE_ADD, null, null)
                                if(score!! < 0.6) {
                                    ttsHelper.speak("Mislim $translated")
                                } else {
                                    ttsHelper.speak(translated)
                                }

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
                        detList.forEach { det ->
                            val boundingBox = det.boundingBox
                            val score = det.categories[0].score
                            val rectF = RectF(
                                boundingBox.left,
                                boundingBox.top,
                                boundingBox.right,
                                boundingBox.bottom
                            )
                            translatedDetections.add(
                                CustomDetection(
                                    rectF,
                                    label,
                                    score,
                                    colorList[colorIndex]
                                )
                            )
                        }
                        ttsHelper.speak(label)
                        Log.i("TTS", "Speaking: $label for label: $label")

                        pendingTranslations--
                        if (pendingTranslations == 0) {
                            fragmentCameraBinding.overlay.setTranslatedResults(
                                translatedDetections,
                                imageHeight,
                                imageWidth
                            )
                        }
                    }
                } else {
                    pendingTranslations--
                }
            }
        }
    }


    fun setTranslator(translator: Translator) {
        this.translator = translator
    }
    override fun onError(error: String) {
        activity?.runOnUiThread {
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()

        cameraProvider?.unbindAll()
        imageAnalyzer?.clearAnalyzer()

        objectDetectorHelper.clearObjectDetector()

        ttsHelper.shutdown()
        cameraExecutor.shutdown()

        _fragmentCameraBinding = null

//        _fragmentCameraBinding = null
//        super.onDestroyView()
//        ttsHelper.shutdown()
//        cameraExecutor.shutdown()
    }


    fun getScannedItems(): List<String> = scannedLabels.toList()
}