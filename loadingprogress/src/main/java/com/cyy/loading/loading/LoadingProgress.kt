package com.cyy.loading.loading

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.support.v4.app.ActivityCompat
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import com.cyy.loading.R


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
        progressDrawable.strokeWidth = dp2px(3)
        progressDrawable.setColorFilter(ActivityCompat.getColor(context , R.color.colorAccent) ,PorterDuff.Mode.SRC )
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

    fun setProgressRotateAngle(angle:Float){
        progressDrawable.rotateAngle = angle
    }

    private fun start(){
        val oa = ObjectAnimator.ofInt(this , "progressLevel" , 0 ,10000 )
        oa.duration = 3000
        oa.repeatMode = ValueAnimator.RESTART
        oa.repeatCount = ValueAnimator.INFINITE
        oa.interpolator = AccelerateDecelerateInterpolator()
        oa.start()

        val rotateOa = ObjectAnimator.ofFloat(this , "progressRotateAngle" , 0f ,360f )
        rotateOa.duration = 6000
        rotateOa.repeatMode = ValueAnimator.RESTART
        rotateOa.repeatCount = ValueAnimator.INFINITE
        rotateOa.interpolator = LinearInterpolator()
        rotateOa.start()
    }

}


private class LoadingProgressDrawable : Drawable() {

    private val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val spotPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var startAngle = -90f
    private var sweepAngle:Float = 0f
    private val rectF:RectF = RectF()
    private var spotOffset = 10

//    /**
//     * 控制画风格  偶数为弧度增加 奇数为弧度减少
//     */
//    private var drawStyle:Int = 0
//        set(value) {
//            if (value==100){
//                field = 0
//            }
//            field = value
//        }

    var beginAngle = -90f //开始的位置
    var orginSweepAngle = 360f //最开始的角度

    var rotateAngle:Float = 0f


    var strokeWidth = 0f
        set(value) {
            field = value
            paint.strokeWidth = value
            spotPaint.strokeWidth = value
        }

    init {
        paint.strokeWidth = strokeWidth
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND

        spotPaint.style = Paint.Style.STROKE
        spotPaint.strokeCap = Paint.Cap.ROUND
        spotPaint.strokeWidth = strokeWidth
    }



    override fun draw(canvas: Canvas) {
        val count = canvas.save()
        val rotate = rotateAngle
//        canvas.rotate(rotate, rectF.centerX() , rectF.centerY())

        val angle = drawProgressBar(canvas)
        drawSpot(canvas , angle)
        canvas.restoreToCount(count)
    }

    /**
     * 画进度条
     */
    private fun drawProgressBar(canvas: Canvas):Angle{
        sweepAngle = 720f * level/10000 + orginSweepAngle
        var actualSweepAngle = sweepAngle
        if (actualSweepAngle>360){
            startAngle = actualSweepAngle - 360 + beginAngle
            actualSweepAngle = 360f - (startAngle - beginAngle )
        }
        canvas.drawArc(rectF , startAngle , actualSweepAngle , false , paint)
        //todo 创建对象不要在onDraw
        return Angle(startAngle , actualSweepAngle)
    }

    /**
     * 画点点
     */
    private fun drawSpot(canvas: Canvas , angle: Angle){
        val count = canvas.save()
        canvas.translate(rectF.centerX() , rectF.centerY())
        drawPoints(canvas , angle.start , 0f)
        canvas.restoreToCount(count)
    }

    /**
     * 计算画出了多少个点点
     *
     * 根据点点的位置设置制定的大小
     */
    private fun drawPoints(canvas: Canvas, start:Float ,sweep: Float){
        var  a = beginAngle

        //计算能话多少个点点
        var count = ((start - a)/spotOffset).toInt()
        count = when{
            count>360/spotOffset -> 360/spotOffset
            else -> count
        }

        var i = 0
        while (a < start){
            a += spotOffset

            if (a > start){
                break
            }
            //计算点点的X Y
            val radus = rectF.centerX()-strokeWidth
            val radians = Math.toRadians(a.toDouble())
            val startX = Math.cos(radians)*radus
            val startY = Math.sin(radians)*radus
            //点点的宽度递增
            spotPaint.strokeWidth = strokeWidth * i.toFloat()/count
            canvas.drawPoint(startX.toFloat() , startY.toFloat() , spotPaint)

            if (a - 360 > beginAngle){
                break
            }
            i ++
        }

        log("connt == $count  i == $i")
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
        spotPaint.colorFilter = colorFilter
    }

    override fun onBoundsChange(bounds: Rect) {
        rectF.left = bounds.left.toFloat()+strokeWidth
        rectF.top = bounds.top.toFloat()+strokeWidth
        rectF.right = bounds.right.toFloat()-strokeWidth
        rectF.bottom = bounds.bottom.toFloat()-strokeWidth

        super.onBoundsChange(bounds)
    }
}

internal data class Angle(val start:Float ,
                          var sweep:Float)
