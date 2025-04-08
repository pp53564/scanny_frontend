package com.scanny_project.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class TextToSpeechHelper(
    context: Context,
    private val languageCode: String = "hr",
    private val onInitialized: (() -> Unit)? = null
) : TextToSpeech.OnInitListener {

    private var textToSpeech: TextToSpeech? = TextToSpeech(context, this)

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech?.apply {
                language = Locale.forLanguageTag("hr")
                setPitch(1.2f)
                setSpeechRate(1.2f)
            }
            onInitialized?.invoke()
        }
    }
    fun speak(message: String) {
        textToSpeech?.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
    }
    fun shutdown() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
    }
}