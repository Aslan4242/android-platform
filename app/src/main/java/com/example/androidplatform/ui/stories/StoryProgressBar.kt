package com.example.androidplatform.ui.stories

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import com.example.androidplatform.R

class StoryProgressBar @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {

    private var loadingPercent = 0f

    private val linePaint = Paint().apply {
        color = resources.getColor(R.color.light_transparent)
    }
    private val progressPaint = Paint().apply {
        color = resources.getColor(R.color.gray_2)
    }

    private val valueAnimator = ValueAnimator().apply {
        interpolator = LinearInterpolator()
        duration = ANIMATION_DURATION
        setFloatValues(0f, 1f)
        addUpdateListener {
            loadingPercent = animatedValue as Float
            invalidate()
        }
        doOnEnd { progressEndListener.invoke() }
    }

    private lateinit var progressEndListener: () -> Unit

    fun setOnProgressEndListener(progressEndListener: () -> Unit) {
        this.progressEndListener = progressEndListener
    }

    fun start() = valueAnimator.start()

    fun pause() = valueAnimator.pause()

    fun resume() = valueAnimator.resume()

    fun cancel() = with(valueAnimator) {
        valueAnimator.pause()
        valueAnimator.setCurrentFraction(0f)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRoundRect(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            height.toFloat(),
            height.toFloat(),
            linePaint
        )

        canvas.drawRoundRect(
            0f,
            0f,
            width.toFloat() * loadingPercent,
            height.toFloat(),
            height.toFloat(),
            height.toFloat(),
            progressPaint
        )
    }

    companion object {
        private const val ANIMATION_DURATION = 7_000L
    }
}
