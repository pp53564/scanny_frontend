package com.scanny_project.utils

import android.util.Log
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions


object TranslatorHelper {
    private var translator: Translator? = null

    fun initializeTranslator(targetLang: String, param: (Translator) -> Unit) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(targetLang)
            .build()

        translator = Translation.getClient(options)


        var conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        translator!!.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                // Model downloaded successfully. Okay to start translating.
                // (Set a flag, unhide the translation UI, etc.)
                param(translator!!)
                Log.d("MLKit", "Language model downloaded successfully!")
            }
            .addOnFailureListener { exception ->
                // Model couldn’t be downloaded or other internal error.
                // ...
                Log.e("MLKit", "Failed to download language model: ${exception.message}")
            }
    }

    fun translateText(text: String, callback: (String) -> Unit) {
        translator?.translate(text)
            ?.addOnSuccessListener { translatedText ->
                callback(translatedText)
            }
            ?.addOnFailureListener { e ->
                Log.e("TranslatorHelper", "Translation failed: ${e.message}")
                callback("Translation error")
            }
    }
}