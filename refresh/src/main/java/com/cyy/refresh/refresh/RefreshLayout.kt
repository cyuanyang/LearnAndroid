package com.cyy.refresh.refresh

import android.animation.ObjectAnimator
import android.content.Context
import android.support.v4.view.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.AbsListView
import android.widget.FrameLayout

/**
 * Created by cyy on 17/9/8.
 *
 */

internal fun ViewGroup.eachChildren(block:(position:Int, child: View)->Unit){
    for (i in (0..childCount-1)){
        block(i , getChildAt(i))
    }
}

//RefreshLayout
class RefreshLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int
) : FrameLayout(context, attrs, defStyleAttr){

    constructor(context: Context) : this(context , null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs , 0)

    var refreshHeader:RefreshHeader? = null
        set(value) {
            field = value
            value?.let { addHeaderLayout(it) }
        }

    var dragRate:Float = 0.5f //下拉时的距离系数
    var animateInterval= 500L //动画的时间间隔

    private var headerView:View? = null
    private var mContentView:View? = null
    var mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop


    private var mInitDownY:Int = 0 //按下时的位置
    private var mInitLocation:Int = 0 //开始下拉时的手指位置
    private var mIsBeginDrag = false
    private var isRefresh = false
    val mScrollCallback:ScrollCallback? = null

    init {
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount != 1){
            throw IllegalStateException("RefreshLayout can host only one direct child")
        }

        mContentView = getChildAt(0)
    }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        if (mContentView==null)return
//        mContentView!!.measure(MeasureSpec.makeMeasureSpec(
//                measuredWidth - paddingLeft - paddingRight,
//                MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
//                measuredHeight - paddingTop - paddingBottom, MeasureSpec.EXACTLY))
//    }

    private fun addHeaderLayout(refreshHeader:RefreshHeader){
        val headerHeight = refreshHeader.getHeaderHeight()
        val lp = LayoutParams(LayoutParams.MATCH_PARENT , headerHeight)
        lp.topMargin = -headerHeight
        headerView = refreshHeader.getHeaderView()
        addView( headerView , lp)
    }

    override fun requestDisallowInterceptTouchEvent(b: Boolean) {
        if (android.os.Build.VERSION.SDK_INT < 21 && mContentView is AbsListView
                || mContentView != null && !ViewCompat.isNestedScrollingEnabled(mContentView)) {
            // Nope.
        } else {
            super.requestDisallowInterceptTouchEvent(b)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {

        val action = ev.action

        if (action == MotionEvent.ACTION_DOWN){
            mInitDownY = ev.y.toInt()
        }
        if (canChildScrollUp()){
            return false
        }

        when(action){
            MotionEvent.ACTION_DOWN -> {
                mInitDownY = ev.y.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                var y = ev.y
                val yDiff = y - mInitDownY
                log("yDiff = $yDiff .... $mTouchSlop")
                if (yDiff>mTouchSlop && !mIsBeginDrag){
                    log("2222222222")
                    mIsBeginDrag = true
                    mInitLocation = y.toInt()
                }
            }
            MotionEvent.ACTION_UP ,
            MotionEvent.ACTION_CANCEL-> {
                mInitDownY = 0
                mInitLocation = 0
                mIsBeginDrag = false
            }
        }

        return mIsBeginDrag
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        when(action){
            MotionEvent.ACTION_DOWN -> {
                mInitDownY = event.y.toInt()

            }
            MotionEvent.ACTION_MOVE -> {
                val y = event.y
                val offset = (y-mInitLocation) * dragRate
                offsetHeaderView(offset)
                mInitLocation = y.toInt()
            }
            MotionEvent.ACTION_UP -> {
                mInitDownY = 0
                mInitLocation = 0
                mIsBeginDrag = false
                isIntoRefresh()
            }
            MotionEvent.ACTION_CANCEL-> {

            }
        }
        return super.onTouchEvent(event)
    }

    private fun offsetHeaderView(offset:Float){
        if (offset == 0F)return
        ViewCompat.offsetTopAndBottom(headerView , offset.toInt())
        ViewCompat.offsetTopAndBottom(mContentView , offset.toInt())
        val dis = ViewCompat.getY(mContentView).toInt()
        dispatchHeaderScrollEvent(dis)
    }

    //是否进入刷新状态
    private fun isIntoRefresh() {
        refreshHeader?.let {
            val headerHeight = it.getHeaderHeight()
            val dis = getScrollEffectiveDistance()
            if (dis>headerHeight){
                isRefresh = true
                smoothScrollTo(headerHeight)
            }else{
                isRefresh = false
                smoothScrollTo(0)
            }
        }
    }
    
    private fun smoothScrollTo(y:Int){
        val startY = getScrollEffectiveDistance()
        val oa = ObjectAnimator.ofInt(startY , y)
        oa.duration = animateInterval
        oa.interpolator = AccelerateDecelerateInterpolator()
        oa.start()
        oa.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            moveTo(value)
        }
    }

    //移动view
    private fun moveBy(dis:Int){
        ViewCompat.offsetTopAndBottom(headerView , dis)
        ViewCompat.offsetTopAndBottom(mContentView , dis)
    }

    private fun moveTo(to:Int){
        val y = getScrollEffectiveDistance()
        moveBy(to - y)
    }

    private fun reset(){
        val dis = ViewCompat.getY(mContentView).toInt()
        ViewCompat.offsetTopAndBottom(headerView , -dis)
        ViewCompat.offsetTopAndBottom(mContentView , -dis)
    }

    //内部的View是否还能往上滑动
    fun canChildScrollUp(): Boolean {
        return ViewCompat.canScrollVertically(mContentView, -1)
    }

    //获取有效的滑动距离
    fun getScrollEffectiveDistance():Int{
        return ViewCompat.getY(mContentView).toInt()
    }

    private fun dispatchHeaderScrollEvent(dis:Int){
        if (dis > refreshHeader!!.getHeaderHeight()){
            isRefresh = true
        }
        mScrollCallback?.scroll(dis)
    }

    private fun log(msg:String){
        Log.i("RefreshLayout",msg)
    }

}

interface RefreshHeader{
    fun getHeaderView():View  //返回header view
    fun getHeaderHeight():Int //header height
}

