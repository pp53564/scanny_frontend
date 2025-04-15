package com.scanny_project.features.camera

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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
//    private var bounds = Rect()



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
        textBackgroundPaint.color = ContextCompat.getColor(context!!, R.color.dark_blue)
        textBackgroundPaint.style = Paint.Style.FILL
        textBackgroundPaint.isAntiAlias = true

        textPaint.color = Color.WHITE
        textPaint.isAntiAlias = true
        textPaint.textSize = 80f
        textPaint.textAlign = Paint.Align.CENTER

        boxPaint.color = ContextCompat.getColor(context!!, R.color.dark_blue)
        boxPaint.strokeWidth = 12f
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
//            val rawTop = boundingBox.top * scaleFactor
//            val top = if (rawTop < 20f) rawTop + 20f else rawTop
            val top = (boundingBox.top * scaleFactor)
                .coerceIn(100f, height.toFloat() - 100f)
            val bottom = boundingBox.bottom * scaleFactor
            val rawLeft = boundingBox.left * scaleFactor
            val left = if (rawLeft < 20f) rawLeft + 20f else rawLeft
            val right = (boundingBox.right * scaleFactor).coerceAtMost(width.toFloat())
            textBackgroundPaint.color = result.color
            boxPaint.color = result.color

            val drawableRect = RectF(left, top, right, bottom)

            val cornerRadiusRect = 32f
            canvas.drawRoundRect(drawableRect, cornerRadiusRect, cornerRadiusRect, boxPaint)

            val label = result.label
            val padding = 20f
            val cornerRadiusText = 15f

            val textWidth = textPaint.measureText(label)
            val textHeight = textPaint.descent() - textPaint.ascent()

            val backgroundRect = RectF(
                left,
                top - textHeight - padding,
                left + textWidth + padding * 2,
                top
            )

            canvas.drawRoundRect(backgroundRect, cornerRadiusText, cornerRadiusText, textBackgroundPaint)

            val textX = backgroundRect.left + (backgroundRect.width() / 2)
            val textY = backgroundRect.top + (backgroundRect.height() / 2) - (textPaint.descent() + textPaint.ascent()) / 2

            canvas.drawText(label, textX, textY, textPaint)
        }

    }

}

