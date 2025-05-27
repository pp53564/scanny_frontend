package com.scanny_project.features.teacher.lecture

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ui_ux_demo.R
import com.example.ui_ux_demo.databinding.ActivityEditTranslationBinding

class EditTranslationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditTranslationBinding

    companion object {
        val EXTRA_POSITION: String? = null
        const val REQUEST_CODE   = 1001
        const val EXTRA_HR       = "extra_hr"
        const val EXTRA_EN       = "extra_en"
        const val EXTRA_DE       = "extra_de"
        const val EXTRA_FR       = "extra_fr"
        const val EXTRA_IT       = "extra_it"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTranslationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.titleText.text = getString(R.string.changeTranslation)
        binding.header.buttonBackLayout.imButtonBack.setOnClickListener {
            finish()
        }

        val hr = intent.getStringExtra(EXTRA_HR).orEmpty()
        binding.tvCroatian.text       = hr
        binding.etEnglish.setText     ( intent.getStringExtra(EXTRA_EN) )
        binding.etGerman .setText     ( intent.getStringExtra(EXTRA_DE) )
        binding.etFrench .setText     ( intent.getStringExtra(EXTRA_FR) )
        binding.etItalian.setText     ( intent.getStringExtra(EXTRA_IT) )

        binding.btnSave.setOnClickListener {
            val data = Intent().apply {
                putExtra(EXTRA_HR, hr)
                putExtra(EXTRA_EN,     binding.etEnglish  .text.toString().trim())
                putExtra(EXTRA_DE,     binding.etGerman   .text.toString().trim())
                putExtra(EXTRA_FR,     binding.etFrench   .text.toString().trim())
                putExtra(EXTRA_IT,     binding.etItalian  .text.toString().trim())
                putExtra(AddLectureActivity.EXTRA_POSITION,
                    intent.getIntExtra(AddLectureActivity.EXTRA_POSITION, -1))
            }
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }
}
