package com.cyy.refresh.refresh.header

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.cyy.refresh.R
import kotlinx.android.synthetic.main.header_default_layout.view.*
import android.R.attr.centerY
import com.cyy.refresh.refresh.*


/**
 * Created by study on 17/9/8.
 *
 */


class RefreshHeaderLayout : RefreshHeader{

    override fun onPulling(distance: Int , progress:Float) {

        log("distance == $distance progress=$progress")
    }

    override fun onRefreshStateChanged(state: RefreshState) {

    }

    override fun getHeaderView(refreshLayout: RefreshLayout): View {
        val headerView = LayoutInflater.from(refreshLayout.context)
                .inflate(R.layout.header_default_layout , refreshLayout , false)

        return headerView
    }

    override fun getHeaderHeight(): Int {
        return 100
    }

}

class ProgressDrawable : Drawable(){

    private val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var progressWidth = 10f
    private var rectF = RectF()


    init {
        paint.color = Color.BLACK
        paint.strokeCap = Paint.Cap.ROUND
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = progressWidth

        textPaint.color = Color.BLACK
        textPaint.textSize = 50f
        textPaint.style = Paint.Style.FILL
        textPaint.textAlign = Paint.Align.CENTER
    }

    override fun draw(canvas: Canvas) {
        log("level == $level")
        val sweepAngle = 360f * level / 10000
        canvas.drawArc(rectF , -90f , sweepAngle ,false , paint)

        var p = (100f * level / 10000).toInt()
        if (p>100){
            p = 100
        }
        val fontMetrics = textPaint.fontMetrics
        val top = fontMetrics.top//为基线到字体上边框的距离,即上图中的top
        val bottom = fontMetrics.bottom//为基线到字体下边框的距离,即上图中的bottom

        val baseLineY = (rectF.centerY() - top / 2 - bottom / 2) //基线中间点的y轴计算公式
        canvas.drawText(p.toString()+"%",rectF.centerX(),baseLineY,textPaint)
    }

    fun setTextSize(textSize:Float){
        textPaint.textSize = textSize
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

    override fun onLevelChange(level: Int): Boolean {
        invalidateSelf()
        return true
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        rectF.left = bounds.left+progressWidth
        rectF.top = bounds.top + progressWidth
        rectF.right = bounds.right - progressWidth
        rectF.bottom = bounds.bottom - progressWidth
    }

}

class ProgressHeaderLayout : RefreshHeader{

    var progressView:View? = null
    var refreshTextView:TextView? = null
    val drawable = ProgressDrawable()

    override fun onPulling(distance: Int , progress:Float) {
        var p = progress
        log("p====$p" )
        if (p>=1){
            refreshTextView!!.text = "松开刷新"
            p = 1.0f
        }else{
            refreshTextView!!.text = "下拉刷新"
        }
        drawable.level = (10000 * p).toInt()
    }

    override fun onRefreshStateChanged(state: RefreshState) {
        when(state){
            RefreshState.REFRESHING->{
                refreshTextView!!.text = "正在刷新..."
            }
            RefreshState.IDLE->{
                refreshTextView!!.text = "刷新完成"
            }
        }
    }

    override fun getHeaderView(refreshLayout: RefreshLayout): View {
        val headerView = LayoutInflater.from(refreshLayout.context)
                .inflate(R.layout.header_default_layout , refreshLayout , false)
        progressView = headerView.findViewById(R.id.progress)

        drawable.setTextSize(refreshLayout.dp2px(10))
        progressView?.setBackgroundDrawable(drawable)

        refreshTextView = headerView.findViewById(R.id.refreshTextView)
        return headerView
    }

    override fun getHeaderHeight(): Int {
        return 80
    }
}


