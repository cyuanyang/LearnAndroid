package com.cyy.loading.loading

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

/**
 * Created by cyy on 17/10/10.
 *
 */


class LoadingProgressLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

}

/**
 * 旋转的进度条View
 */
class LoadingProgress @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var progressDrawable = LoadingProgressDrawable()

    init {
        progressDrawable.strokeWidth = dp2px(2)
        progressDrawable.callback = this
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        progressDrawable.setBounds(0,0,measuredWidth,measuredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //画进度条
        progressDrawable.draw(canvas)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        start()
    }

    fun setProgressLevel(level: Int){
        progressDrawable.level = level
        log("level === $level")
        postInvalidateOnAnimation()
    }

    private fun start(){
        val oa = ObjectAnimator.ofInt(this , "progressLevel" , 0 ,10000 )
        oa.duration = 2000
        oa.repeatMode = ValueAnimator.RESTART
        oa.repeatCount = ValueAnimator.INFINITE
        oa.start()
    }

}


private class LoadingProgressDrawable : Drawable() {

    private val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var sweepAngle:Float = 0f
    private val rectF:RectF = RectF()

    var strokeWidth = 0f
        set(value) {
            field = value
            paint.strokeWidth = value
        }

    init {
        paint.color = Color.RED
        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
    }

    override fun draw(canvas: Canvas) {
        sweepAngle = 360f * level/10000
        log("sweepAngle === $sweepAngle")
        canvas.drawArc(rectF , -90f , sweepAngle , false , paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun onBoundsChange(bounds: Rect) {
        rectF.left = bounds.left.toFloat()+strokeWidth
        rectF.top = bounds.top.toFloat()+strokeWidth
        rectF.right = bounds.right.toFloat()-strokeWidth
        rectF.bottom = bounds.bottom.toFloat()-strokeWidth
        super.onBoundsChange(bounds)
    }

}