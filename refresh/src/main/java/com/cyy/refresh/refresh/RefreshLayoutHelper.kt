package com.cyy.refresh.refresh

import android.content.Context
import android.support.v4.view.ViewCompat
import android.view.VelocityTracker
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.Scroller
import android.widget.ScrollView
import android.os.Build
import android.view.View
import android.widget.AbsListView

/**
 * Created by study on 17/9/12.
 *
 *
 */
internal class RefreshLayoutHelper(context:Context) {

    private val MIN_VELOCITY = ViewConfiguration.get(context).scaledMinimumFlingVelocity
    private val MXN_VELOCITY = ViewConfiguration.get(context).scaledMaximumFlingVelocity

    private val mScroller = Scroller(context)
    private var mVelocityTracker: VelocityTracker? = null

    private var mLastY = 0

    fun initVelocityTrackerIfNotExists(event: MotionEvent) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker?.addMovement(event)
    }

    fun recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker?.recycle()
            mVelocityTracker = null
        }
    }

    fun abortAnimationFinished(){
        if (!mScroller.isFinished)
            mScroller.abortAnimation()
    }

    /**
     * 在刷新的状态的时候  需要header部分和下面的Content部分看起来像一个完成的列表
     *
     * 所以这里只有处在刷新的状态的时候才会执行
     *
     * 这里的逻辑
     * 1. 当header部分可见的时候 ， 滑动整个RefreshLayout header 和 content 一起做拖动,fling运动
     * 2. 向上拖动的时候，当header滚动到屏幕外面的时候，Scroller将放弃此次计算滚动 将这个计算放弃这一时刻的滚动状态
     * 传给可以滚动的ContentView,让ContentView继续滚动
     * 3. 向下拖动的时候，当mContentView 若滑动到了顶部，这时候将Scroller的滚动状态传给header，让header继续滚动，
     * 同时终止向ContentView分发事件
     */
    fun computeScroll(refreshLayout: RefreshLayout){

        //如果不处在刷新的状态则 不再处理fling
        if (refreshLayout.state == RefreshState.IDLE){
            mScroller.abortAnimation()
            refreshLayout.scrollState = ScrollState.IDEA
        }

        if (mScroller.computeScrollOffset()){
            val currY = mScroller.currY
            var deltaY = (currY - mLastY)
            val scrollY = refreshLayout.getScrollEffectiveDistance()
            log("currY = $currY  deltaY = $deltaY  scrollY = $scrollY")
            if (scrollY > -refreshLayout.mHeaderHeight
                    && scrollY < 0){
                //1.
                if (scrollY - deltaY < -refreshLayout.mHeaderHeight){
                    deltaY = refreshLayout.mHeaderHeight + scrollY
                    mScroller.abortAnimation()
                }
                refreshLayout.scrollBy(0 , -deltaY)

                mLastY = currY
            }else{
                //2.
                if (deltaY<0){
                    val distance = mScroller.finalY - currY
                    val duration = mScroller.getDuration()- mScroller.timePassed()

                    smoothScrollBy(refreshLayout.mContentView!!, mScroller.currVelocity.toInt(), distance, duration)
                    mScroller.abortAnimation()
                }else{
                    //3.
                    if (!refreshLayout.canChildScrollUp()){
                        if (scrollY - deltaY < -refreshLayout.mHeaderHeight){
                            deltaY = refreshLayout.mHeaderHeight + scrollY
                            mScroller.abortAnimation()
                        }
                        refreshLayout.scrollBy(0 , -deltaY)
                    }
                }
            }

            mLastY = currY
            ViewCompat.postInvalidateOnAnimation(refreshLayout)
        }
    }

    //这里控制内容View的Fling 动作
    private fun smoothScrollBy(scrollableView: View, velocityY: Int, distance: Int, duration: Int) {
        if (scrollableView is AbsListView) {
            if (Build.VERSION.SDK_INT >= 21) {
                scrollableView.fling(velocityY)
            } else {
                scrollableView.smoothScrollBy(distance, duration)
            }
        } else if (scrollableView is ScrollView) {
            scrollableView.fling(velocityY)
        }
    }

    fun fling(startX: Int, startY: Int, minX: Int, maxX: Int, minY: Int, maxY: Int){
        mVelocityTracker?.let {
            it.computeCurrentVelocity(1000 , MXN_VELOCITY.toFloat())
            val vy = it.yVelocity
            if (Math.abs(vy) > MIN_VELOCITY){
                mLastY = startY
                mScroller.fling(startX , startY ,0 , vy.toInt(), minX , maxX , minY , maxY)
            }
        }
    }

}
