package com.scanny_project.features.camera

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.ui_ux_demo.R
import com.scanny_project.data.model.CustomDetection
import java.util.LinkedList
import kotlin.math.max


class OverlayView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var results: List<CustomDetection> = LinkedList<CustomDetection>()
    private var boxPaint = Paint()
    private var textBackgroundPaint = Paint()
    private var textPaint = Paint()
    private var scaleFactor: Float = 1f
    private var bounds = Rect()


    init {
        initPaints()
    }

    fun clear() {
        textPaint.reset()
        textBackgroundPaint.reset()
        boxPaint.reset()
        invalidate()
        initPaints()
    }

    private fun initPaints() {
        textBackgroundPaint.color = Color.argb(160, 0, 0, 0)
        textBackgroundPaint.style = Paint.Style.FILL
        textBackgroundPaint.isAntiAlias = true
//        textBackgroundPaint.textSize = 62f

        // White text on top of the black background
        textPaint.color = Color.WHITE
//        textPaint.style = Paint.Style.FILL
        textPaint.isAntiAlias = true
        textPaint.textSize = 62f

        // Dark-blue bounding box with a moderate stroke width
        boxPaint.color = ContextCompat.getColor(context!!, R.color.dark_blue)
        boxPaint.strokeWidth = 10f
        boxPaint.style = Paint.Style.STROKE
        boxPaint.isAntiAlias = true
    }


    fun setTranslatedResults(
        detectionResults: List<CustomDetection>,
        imageHeight: Int,
        imageWidth: Int
    ) {
        results = detectionResults
        scaleFactor = max(width * 1f / imageWidth, height * 1f / imageHeight)
        invalidate()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        for (result in results) {
            val boundingBox = result.boundingBox
            val top = boundingBox.top * scaleFactor
            val bottom = boundingBox.bottom * scaleFactor
            val left = boundingBox.left * scaleFactor
            val right = boundingBox.right * scaleFactor

            val drawableRect = RectF(left, top, right, bottom)
            canvas.drawRect(drawableRect, boxPaint)
//            canvas.drawRect(result.boundingBox, boxPaint)
            // measure text
            textBackgroundPaint.getTextBounds(result.label, 0, result.label.length, bounds)
            val textWidth = bounds.width()
            val textHeight = bounds.height()


            // background rect
            canvas.drawRect(
                left,
                top,
                left + textWidth + 8,
                top + textHeight + 8,
                textBackgroundPaint
            )
            canvas.drawText(result.label, left, top + textHeight, textPaint)
        }
    }

//    fun setResults(
//        detectionResults: MutableList<Detection>,
//        imageHeight: Int,
//        imageWidth: Int,
//    ) {
//
//        results = myDetections
//
//
//        // PreviewView is in FILL_START mode. So we need to scale up the bounding box to match with
//        // the size that the captured images will be displayed.
//        scaleFactor = max(width * 1f / imageWidth, height * 1f / imageHeight)
//    }

//    companion object {
//        private const val BOUNDING_RECT_TEXT_PADDING = 8
//    }

//    fun setCustomTranslator(translator: Translator) {
//        this.translator = translator
//    }
}
