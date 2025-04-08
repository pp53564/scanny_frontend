package com.scanny_project.features.changePassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.ui_ux_demo.databinding.ActivityChangePasswordBinding
import androidx.lifecycle.Observer
import com.scanny_project.features.home.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordActivity : AppCompatActivity() {
    private val viewModel: ChangePasswordViewModel by viewModels()
    private lateinit var binding: ActivityChangePasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val oldPassword = binding.oldPassword
        val newPassword = binding.newPassword
        val btnChangePassword = binding.changePassword

        btnChangePassword.setOnClickListener {
            val oldPassword = oldPassword.text.toString().trim()
            val newPassword = newPassword.text.toString().trim()

            if (oldPassword.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(this, "Molimo popunite sva polja", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.changePassword(oldPassword, newPassword)
        }

        viewModel.changePasswordResult.observe(this, Observer { result ->
            result?.let {
                if (it.error != null) {
                    val errorMsg = translateErrorToCroatian(it.error)
                    Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
                    showChangePasswordFailed(errorMsg)
                } else if (it.success != null) {
                    Toast.makeText(
                        this,
                        "Lozinka je uspješno promijenjena",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUiWithSuccess(it.success)
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
        })
    }
    private fun translateErrorToCroatian(errorMessage: String): String {
        return when {
            errorMessage.contains("Invalid old password", ignoreCase = true) ->
                "Neispravna stara lozinka"
            errorMessage.contains("User not found", ignoreCase = true) ->
                "Korisnik nije pronađen"
            errorMessage.contains("Error changing password", ignoreCase = true) ->
                "Došlo je do pogreške prilikom promjene lozinke"
            else -> "Došlo je do pogreške: $errorMessage"
        }
    }

    private fun showChangePasswordFailed(errorString: String) {
        binding.tvMessage.apply {
            text = errorString
            setTextColor(context.getColor(android.R.color.holo_red_dark))
            visibility = android.view.View.VISIBLE
        }
    }

    private fun updateUiWithSuccess(message: String) {
        binding.tvMessage.apply {
            text = message
            setTextColor(context.getColor(android.R.color.holo_green_dark))
            visibility = android.view.View.VISIBLE
        }
    }
}
