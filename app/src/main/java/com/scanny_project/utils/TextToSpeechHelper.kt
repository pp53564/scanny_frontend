package com.scanny_project.utils

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import java.util.Locale

class TextToSpeechHelper(
    context: Context,
    private val languageCode: String = "hr",
    private val onInitialized: (() -> Unit)? = null
) : TextToSpeech.OnInitListener {

    // Public read-only property to check if TTS is currently speaking
    @Volatile
    var isSpeaking: Boolean = false
        private set

    private var textToSpeech: TextToSpeech? = TextToSpeech(context, this)

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech?.apply {
                language = Locale.forLanguageTag(languageCode)
                setPitch(1.2f)
                setSpeechRate(1.2f)

                // Attach an UtteranceProgressListener to track speech start/end
                setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        this@TextToSpeechHelper.isSpeaking = true
                        Log.d("TextToSpeechHelper", "TTS started. UtteranceID: $utteranceId")
                    }
                    override fun onDone(utteranceId: String?) {
                        this@TextToSpeechHelper.isSpeaking = false
                        Log.d("TextToSpeechHelper", "TTS done. UtteranceID: $utteranceId")
                    }
                    override fun onError(utteranceId: String?) {
                        this@TextToSpeechHelper.isSpeaking = false
                        Log.e("TextToSpeechHelper", "TTS error. UtteranceID: $utteranceId")
                    }
                })
            }
            onInitialized?.invoke()
        } else {
            Log.e("TextToSpeechHelper", "TTS initialization failed with status: $status")
        }
    }

    fun speak(message: String) {
        if (textToSpeech == null) {
            Log.e("TextToSpeechHelper", "TTS is not initialized or already shut down.")
            return
        }
        val params = Bundle()
        textToSpeech?.speak(message, TextToSpeech.QUEUE_FLUSH, params, "UtteranceID-$message")
    }

    fun shutdown() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
    }
}
