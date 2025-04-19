package com.scanny_project.features.quiz

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import com.example.ui_ux_demo.R
import com.example.ui_ux_demo.databinding.ActivityImageClassificationAndQuizBinding
import com.scanny_project.features.language.SelectLanguageActivityForImageClassification
import com.scanny_project.utils.TextToSpeechHelper
import com.scanny_project.utils.TranslatorHelper
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ImageClassificationAndQuizActivity : AppCompatActivity(){

    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityImageClassificationAndQuizBinding
    private lateinit var imageView: ImageView
//    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private var currentQuestionId: Long = 0L
    private val viewModel: ImageQuizViewModel by viewModels()
    private lateinit var langCode: String
    private lateinit var ttsHelper: TextToSpeechHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageClassificationAndQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageView = binding.imageView

        val task = intent.getStringExtra("QUESTION_KEYWORD") ?: "unknown"
        langCode = intent.getStringExtra("SELECTED_LANGUAGE")!!
        binding.tvThingForPicture.text = task
        currentQuestionId = intent.getLongExtra("QUESTION_ID", 0L)

        ttsHelper = TextToSpeechHelper(this, languageCode = langCode)
        binding.speakerIcon.setOnClickListener{
            ttsHelper.speak(task)
        }

//        imageClassifierHelper = ImageClassifierHelper(
//            context = this,
//            imageClassifierListener = this,
//            threshold = 0.2f,
//        )

        setupListeners()
        setupCameraLauncher()
        observeViewModel()
    }

    private fun observeViewModel() {
        // Observing attemptSent just for logs
        viewModel.attemptSent.observe(this) { sent ->
            Log.i("ImageQuizActivity", "Attempt sent: $sent")
        }

        viewModel.attemptResponse.observe(this) { response ->
            response?.let {
//                if (it.matchedLabel.isNotBlank() && it.confidenceScore > 0f) {
                    handleResult(it.correct, it.confidenceScore, it.matchedLabel)
//                }
            }
        }

        viewModel.loading.observe(this) { isLoading ->
            binding.progressOverlay.visibility =
                if (isLoading) View.VISIBLE else View.GONE
        }
    }


    private fun setupListeners() {
        binding.buttonTakePicture.setOnClickListener {
            openCamera()
        }

        binding.buttonBackLayout.imButtonBack.setOnClickListener {
            if (viewModel.attemptSent.value == false) {
                viewModel.sendAttempt(currentQuestionId, imageBitmap = null, langCode)
            }
            val intent = Intent(this, SelectLanguageActivityForImageClassification::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun setupCameraLauncher() {
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null && data.extras != null) {
                    var image = data.extras!!.get("data") as Bitmap
                    imageView.setImageBitmap(image)
                    imageView.visibility = View.VISIBLE

                    val constraintSet = ConstraintSet()
                    constraintSet.clone(binding.imageContainer)
                    constraintSet.connect(binding.tvQuestion.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                    constraintSet.connect(binding.tvQuestion.id, ConstraintSet.BOTTOM, binding.imageView.id, ConstraintSet.TOP)
                    constraintSet.applyTo(binding.imageContainer)

                    image = Bitmap.createScaledBitmap(image, 224, 224, false)
                    val capturedBitmap = Bitmap.createScaledBitmap(image, 224, 224, false)
                    viewModel.sendAttempt(
                        questionId = currentQuestionId,
                        imageBitmap = capturedBitmap,
                        langCode = langCode
                    )
                }
            }
        }
    }

    private fun openCamera() {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(intent)
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
        }
    }


    private fun handleResult(correct: Boolean, confidenceScore: Float, matchedLabel: String) {
        binding.resultReaction.visibility = View.VISIBLE
        binding.resultReactionIcon.setImageResource( if(correct) R.drawable.baseline_auto_awesome_24 else R.drawable.baseline_arrow_downward_24)
        binding.resultReactionIcon.visibility = View.VISIBLE
        binding.myCardView.visibility = View.VISIBLE
        if (correct) {
            binding.buttonTakePicture.visibility = View.GONE
            binding.resultReaction.text = getString(R.string.correct_answer)
            showImageDialog(true)
            binding.resultIcon2.visibility = View.VISIBLE
            binding.confidencesText.visibility = View.VISIBLE
            binding.confidence.visibility = View.VISIBLE
            binding.confidence.text = "${(confidenceScore * 100).toInt()}%"
        } else {
            TranslatorHelper.initializeTranslator(com.google.mlkit.nl.translate.TranslateLanguage.CROATIAN) {
                TranslatorHelper.translateText(matchedLabel) { translatedText ->
                    binding.resultIcon.visibility = View.VISIBLE
                    binding.classified.visibility = View.VISIBLE
                    binding.result.visibility = View.VISIBLE
                    binding.result.text = translatedText
                }
            }
            binding.buttonTakePicture.text = getString(R.string.try_again)
            binding.resultReaction.text = getString(R.string.wrong_answer_2)
            showImageDialog(false)
        }
    }

    private fun showImageDialog(correct: Boolean) {
        if (isFinishing || isDestroyed) return
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_image)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val textView = dialog.findViewById<TextView>(R.id.dialogMessage)
        textView.text = if (correct) {
            getString(R.string.correct_answer)
        } else {
            getString(R.string.wrong_answer)
        }
        val image = dialog.findViewById<ImageView>(R.id.dialogImageView)
        image.setImageResource(R.drawable.scanny_from_bottom)

        dialog.show()
        dialog.window?.decorView?.postDelayed({
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }, 3000)
    }

//    override fun onPause() {
//        super.onPause()
////        if (viewModel.attemptSent.value == false) {
////            Log.i("ImageQuizActivity", "Sending final attempt before pause")
////            viewModel.sendAttempt(currentQuestionId, imageBitmap = null)
////        }
//    }

override fun onDestroy() {
    super.onDestroy()
    ttsHelper.shutdown()
}


}
