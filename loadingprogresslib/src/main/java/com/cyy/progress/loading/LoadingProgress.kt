package com.cyy.progress.loading

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import com.cyy.progress.R

/**
 * Created by cyy on 17/10/10.
 *
 */

/**
 * 旋转的进度条View
 */
class LoadingProgress constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    constructor(context: Context):this(context , null )
    constructor(context: Context , attrs: AttributeSet?):this(context , attrs , 0)

    private var progressDrawable = LoadingProgressDrawable()
    private var isRunning = false
    private var progressAnimate:ValueAnimator? = null //进度条动画
    private var rotateAnimate:ValueAnimator? = null //旋转动画
    private var isAutoRunning = true //显示的时候自动开始
    private var isReset = false
    private var progressDuration = 3000L //进度条的时间
    private var rotateDuration = 6000L //旋转的时间

    private var atOnceEnd = false  //调用reset的时候立刻结束

    var listener:ProgressListener? = null

    init {
        var color = resources.getColor(android.R.color.holo_red_light)
        var strokeWidth = dp2px(3)
        if (attrs!=null){
            val a = context.obtainStyledAttributes(attrs , R.styleable.LoadingProgress)
            color = a.getColor(R.styleable.LoadingProgress_c_progressColor , color)
            strokeWidth = a.getDimension(R.styleable.LoadingProgress_c_strokeWidth , strokeWidth)
            isAutoRunning = a.getBoolean(R.styleable.LoadingProgress_c_autoRunning , isAutoRunning)
            atOnceEnd = a.getBoolean(R.styleable.LoadingProgress_c_atOnceEnd , atOnceEnd)

            progressDuration = a.getInteger(R.styleable.LoadingProgress_c_progressDuration ,
                    progressDuration.toInt()).toLong()

            rotateDuration = a.getInteger(R.styleable.LoadingProgress_c_rotateDuration ,
                    rotateDuration.toInt()).toLong()
            a.recycle()
        }

        progressDrawable.strokeWidth = strokeWidth
        progressDrawable.setColorFilter(color ,PorterDuff.Mode.SRC )
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
        if (visibility != View.VISIBLE || windowVisibility != View.VISIBLE) {
            return
        }
        if (!isRunning && isAutoRunning){
            start()
        }
    }

    private fun setProgressLevel(level: Int){
        progressDrawable.level = level
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            postInvalidateOnAnimation()
        }else{
            invalidate()
        }

    }

    private fun setProgressRotateAngle(angle:Float){
        progressDrawable.rotateAngle = angle
    }

    /**
     * 开始动画
     */
    fun start(){
        listener?.onProgressStartListener(this)
        startAnimtor()
    }

    private fun startAnimtor(){
        if (isRunning){
            return
        }
        progressAnimate = ValueAnimator.ofInt( 0 , 10000 )
        progressAnimate!!.duration = progressDuration
        progressAnimate!!.repeatMode = ValueAnimator.RESTART
        progressAnimate!!.repeatCount = ValueAnimator.INFINITE
        progressAnimate!!.interpolator = AccelerateDecelerateInterpolator()
        progressAnimate!!.start()
        progressAnimate!!.addUpdateListener {
            val value = it.animatedValue as Int
            setProgressLevel(value)
        }

        progressAnimate!!.addListener(object : AnimatorListenerAdapter(){
            override fun onAnimationRepeat(animation: Animator?) {
                if (isReset){
                    stop()
                }else{
                    listener?.onProgressRepeatListener(this@LoadingProgress)
                }
            }

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)

                log("onAnimationEnd")

                listener?.onProgressEndListener(this@LoadingProgress)
            }
        })

        rotateAnimate = ValueAnimator.ofFloat( 0f ,360f )
        rotateAnimate!!.duration = rotateDuration
        rotateAnimate!!.repeatMode = ValueAnimator.RESTART
        rotateAnimate!!.repeatCount = ValueAnimator.INFINITE
        rotateAnimate!!.interpolator = LinearInterpolator()
        rotateAnimate!!.start()
        rotateAnimate!!.addUpdateListener {
            val value = it.animatedValue as Float
            setProgressRotateAngle(value)
        }

        isRunning = true
    }

    /**
     * 立刻停止动画
     */
    private fun stop(){
        if (isRunning){
            progressAnimate?.cancel()
            rotateAnimate?.cancel()
            isRunning = false
            isReset = false
        }
    }

    private fun end(){
        if (isRunning){
            progressAnimate?.end()
            rotateAnimate?.end()
            isRunning = false
            isReset = false
        }
    }

    fun reset(){
        if (isRunning){
            listener?.onProgressResetListener(this)
            if (atOnceEnd){
                end()
            }else{
                isReset = true
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stop()
    }
}


private class LoadingProgressDrawable : Drawable() {

    private val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val spotPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var startAngle = -90f
    private var sweepAngle:Float = 0f
    private val rectF:RectF = RectF()
    private var spotOffset = 10
    private var minSpotStrokeWidth = 4 //最小的

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
        canvas.rotate(rotate, rectF.centerX() , rectF.centerY())

        val angle = drawProgressBar(canvas)
        drawSpot(canvas , angle)
        canvas.restoreToCount(count)
    }

    /**
     * 画进度条
     */
    private fun drawProgressBar(canvas: Canvas): Angle {
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
        drawPoints(canvas , angle.start , angle.sweep)
        canvas.restoreToCount(count)
    }

    /**
     * 计算画出了多少个点点
     *
     * 根据点点的位置设置制定的大小
     */
    private fun drawPoints(canvas: Canvas, start:Float ,sweep: Float){
        if (start - 360 > beginAngle){
            drawSpotWhenDecrease(canvas , start , sweep)
        }else{
            //第一圈
            drawSpotWhenIncrease(canvas , start , sweep)
        }
    }

    /**
     * 画出第一圈的点点
     */
    private fun drawSpotWhenIncrease(canvas: Canvas, start:Float ,sweep: Float) {
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
            i ++
            if (a - 360 > beginAngle){
                break
            }
        }
    }

    private fun drawSpotWhenDecrease(canvas: Canvas, start:Float ,sweep: Float){

        var a = 720 + beginAngle

        //计算能话多少个点点
        var count = ((a - start)/spotOffset).toInt()
        val maxCount = 360/spotOffset
        count = when{
            count>maxCount-> 360/spotOffset
            else -> count
        }

        val maxStroke = count.toFloat()/maxCount*(strokeWidth-minSpotStrokeWidth) + minSpotStrokeWidth

        var i = 0

        while (a>start){
            a -= spotOffset
            if (a < start){
                break
            }
            //计算点点的X Y
            val radus = rectF.centerX()-strokeWidth
            val radians = Math.toRadians(a.toDouble())
            val startX = Math.cos(radians)*radus
            val startY = Math.sin(radians)*radus

            spotPaint.strokeWidth = maxStroke - maxStroke * i.toFloat()/count
            canvas.drawPoint(startX.toFloat() , startY.toFloat() , spotPaint)
            i++
        }
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
