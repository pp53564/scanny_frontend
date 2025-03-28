package com.scanny_project

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import com.example.ui_ux_demo.R
import com.example.ui_ux_demo.databinding.ActivityImageClassificationAndQuizBinding
import dagger.hilt.android.AndroidEntryPoint
import org.tensorflow.lite.task.vision.classifier.Classifications
import kotlin.math.min

@AndroidEntryPoint
class ImageClassificationAndQuizActivity : AppCompatActivity(){

    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityImageClassificationAndQuizBinding
    private lateinit var imageView: ImageView
//    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private var currentQuestionId: Long = 0L
    private val viewModel: ImageQuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageClassificationAndQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageView = binding.imageView

        val task = intent.getStringExtra("QUESTION_KEYWORD") ?: "unknown"
        binding.tvThingForPicture.text = task

        currentQuestionId = intent.getLongExtra("QUESTION_ID", 0L)
        Log.i("question_id", currentQuestionId.toString())

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
                // Show a dialog based on "correct" or not
                handleResult(it.correct, it.confidenceScore, it.matchedLabel)
            }
        }
    }


    private fun setupListeners() {
        binding.buttonTakePicture.setOnClickListener {
            openCamera()
        }

        binding.buttonBackLayout.imButtonBack.setOnClickListener {
            if (viewModel.attemptSent.value == false) {
                viewModel.sendAttempt(currentQuestionId, imageBitmap = null)
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

                    binding.buttonTakePicture.visibility = View.GONE

                    image = Bitmap.createScaledBitmap(image, 224, 224, false)
                    val capturedBitmap = Bitmap.createScaledBitmap(image, 224, 224, false)
                    viewModel.sendAttempt(
                        questionId = currentQuestionId,
                        imageBitmap = capturedBitmap
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

//    private fun classifyImage(image: Bitmap) {
//        val resizedImage = Bitmap.createScaledBitmap(image, 224, 224, false)
//
//    }

//    override fun onError(error: String) {
//        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
//    }

//    override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
//        binding.classified.visibility = View.VISIBLE
//        binding.confidencesText.visibility = View.VISIBLE
//        binding.resultReaction.visibility = View.VISIBLE
//        binding.myCardView.visibility = View.VISIBLE
//
//        var succeeded = false
//        if (!results.isNullOrEmpty()) {
//            val classifications = results[0]
//            if (classifications.categories.isNotEmpty()) {
//                val topResult = classifications.categories.maxByOrNull { it.score }
//                topResult?.let {
//                    if (it.label.contains(binding.tvThingForPicture.text)) {
//                        binding.resultReaction.text = "BRAVO! Točan odgovor."
//                        showImageDialog(true)
//                        succeeded = true
//                    } else {
//                        binding.resultReaction.text = "Pokušaj ponovo."
//                        showImageDialog(false)
//                    }
//                    binding.result.text = "${it.label}"
//                    binding.confidence.text = "${it.score * 100}%"
//                    Log.i("ImageQuizActivity", "Label: ${it.label}, Confidence: ${it.score}")
//                }
//            } else {
//                binding.result.text = "Nema rezultata"
//                Log.i("ImageQuizActivity", "No classifications received.")
//            }
//        }
//        viewModel.sendAttempt(currentQuestionId, succeeded, (binding.imageView.drawable as? BitmapDrawable)?.bitmap)
//    }

    private fun handleResult(correct: Boolean, confidenceScore: Float, matchedLabel: String) {
        binding.classified.visibility = View.VISIBLE
        binding.confidencesText.visibility = View.VISIBLE
        binding.resultReaction.visibility = View.VISIBLE
        binding.myCardView.visibility = View.VISIBLE
        if (correct) {
            binding.resultReaction.text = "BRAVO! Točan odgovor."
            showImageDialog(true)
        } else {
            binding.resultReaction.text = "Pokušaj ponovo."
            showImageDialog(false)
        }
        //ovo napravi ako nije tocno sto je prvo bilo gore:
        binding.result.text = "${matchedLabel}"
        binding.confidence.text = "${confidenceScore * 100}%"

    }

    private fun showImageDialog(correct: Boolean) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_image)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val textView = dialog.findViewById<TextView>(R.id.dialogMessage)
        textView.text = if (correct) {
            "Bravo! Dobro obavljeno!"
        } else {
            "Ops! Nije dobro. Pokušaj ponovo."
        }

        val image = dialog.findViewById<ImageView>(R.id.dialogImageView)
        image.setImageResource(if (correct) R.drawable.scanny_happy else R.drawable.scanny_sad)

        dialog.show()
        dialog.window?.decorView?.postDelayed({
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }, 3000)
    }

    override fun onPause() {
        super.onPause()
//        if (viewModel.attemptSent.value == false) {
//            Log.i("ImageQuizActivity", "Sending final attempt before pause")
//            viewModel.sendAttempt(currentQuestionId, imageBitmap = null)
//        }
    }

}
