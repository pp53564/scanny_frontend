package com.scanny_project

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import com.example.ui_ux_demo.databinding.ActivityImageClassificationAndQuizBinding
import org.tensorflow.lite.task.vision.classifier.Classifications
import kotlin.math.min

class ImageClassificationAndQuizActivity : AppCompatActivity(), ImageClassifierHelper.ClassifierListener {
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityImageClassificationAndQuizBinding
    private lateinit var imageView: ImageView
    private lateinit var imageClassifierHelper: ImageClassifierHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageClassificationAndQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageView = binding.imageView

        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            imageClassifierListener = this,
            threshold = 0.2f,
        )
        binding.buttonTakePicture.setOnClickListener {
            openCamera()
        }

        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null && data.extras != null) {
                    var image = data.extras!!.get("data") as Bitmap
                    val dimension = min(image.width, image.height)
//                    image = ThumbnailUtils.extractThumbnail(image, dimension, dimension)
                    imageView.setImageBitmap(image)
                    imageView.visibility = View.VISIBLE
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(binding.imageContainer)

                    constraintSet.connect(binding.tvQuestion.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                    constraintSet.connect(binding.tvQuestion.id, ConstraintSet.BOTTOM, binding.imageView.id, ConstraintSet.TOP)

                    constraintSet.applyTo(binding.imageContainer)

                    binding.buttonTakePicture.visibility = View.GONE

                    image = Bitmap.createScaledBitmap(image, 224, 224, false)
                    classifyImage(image)
                }
            }
        }
    }



    private fun openCamera() {
//        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(intent)
        } else {
            //Request camera permission if we don't have it.
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
        }
    }

//    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == 1 && resultCode == RESULT_OK) {
//            var image = data!!.extras!!["data"] as Bitmap?
//            val dimension =
//                min(image!!.width.toDouble(), image.height.toDouble()).toInt()
//            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension)
//            imageView.setImageBitmap(image)
//
//            image = Bitmap.createScaledBitmap(image, 224, 224, false)
////            classifyImage(image)
//        }
//        super.onActivityResult(requestCode, resultCode, data)
//    }


    private fun classifyImage(image: Bitmap) {
        val resizedImage = Bitmap.createScaledBitmap(image, 224, 224, false)
        imageClassifierHelper.classify(resizedImage, 0)
    }

    override fun onError(error: String) {
            Toast.makeText(this,error, Toast.LENGTH_SHORT).show()
    }

    override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
        binding.classified.visibility = View.VISIBLE
        binding.confidencesText.visibility = View.VISIBLE

        if (results != null && results.isNotEmpty()) {
            val classifications = results[0]

            if (classifications.categories.isNotEmpty()) {
                val topResult = classifications.categories.maxByOrNull { it.score }
                topResult?.let {
                    // Display the label and confidence
                    binding.result.text = "${it.label}"
                    binding.confidence.text = "${it.score * 100}%"
                    Log.i("PETRA", "Label: ${it.label}, Confidence: ${it.score}")
                }
            } else {
                binding.result.text = "Nema rezultata"
                Log.i("PETRA", "No classifications received.")
            }
        }
    }

}