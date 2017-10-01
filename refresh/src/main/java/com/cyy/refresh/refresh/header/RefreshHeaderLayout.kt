package com.cyy.refresh.refresh.header

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.cyy.refresh.R
import com.cyy.refresh.refresh.RefreshHeader
import com.cyy.refresh.refresh.RefreshLayout
import com.cyy.refresh.refresh.RefreshState
import com.cyy.refresh.refresh.log
import kotlinx.android.synthetic.main.header_default_layout.view.*

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

    private var progressWidth = 10f
    private var rectF = RectF()

    init {
        paint.color = Color.BLACK
        paint.strokeCap = Paint.Cap.ROUND
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = progressWidth
    }

    override fun draw(canvas: Canvas) {
        log("level == $level")
        val sweepAngle = 360f * level / 10000
        canvas.drawArc(rectF , -90f , -sweepAngle ,false , paint)
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
    val drawable = ProgressDrawable()

    override fun onPulling(distance: Int , progress:Float) {
        var p = progress
        if (p>=1){
            p = 1.0f
        }
        drawable.level = (10000 * p).toInt()
    }

    override fun onRefreshStateChanged(state: RefreshState) {

    }

    override fun getHeaderView(refreshLayout: RefreshLayout): View {
        val headerView = LayoutInflater.from(refreshLayout.context)
                .inflate(R.layout.header_default_layout , refreshLayout , false)
        progressView = headerView.findViewById(R.id.progress)
        progressView?.setBackgroundDrawable(drawable)
        return headerView
    }

    override fun getHeaderHeight(): Int {
        return 80
    }
}


