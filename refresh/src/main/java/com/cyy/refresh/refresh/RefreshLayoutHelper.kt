package com.cyy.refresh.refresh

import android.content.Context
import android.support.v4.view.ViewCompat
import android.view.VelocityTracker
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.ListView
import android.widget.OverScroller
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

    val MIN_VELOCITY = ViewConfiguration.get(context).scaledMinimumFlingVelocity
    val MXN_VELOCITY = ViewConfiguration.get(context).scaledMaximumFlingVelocity

    val mScroller = Scroller(context)
    var mVelocityTracker: VelocityTracker? = null

    var mLastY = 0

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

    fun computeScroll(refreshLayout: RefreshLayout){
        if (mScroller.computeScrollOffset()){
            val currY = mScroller.currY
            var deltaY = (currY - mLastY)
            val scrollY = refreshLayout.getScrollEffectiveDistance()
            if (scrollY > -refreshLayout.mHeaderHeight){
                if (scrollY - deltaY < -refreshLayout.mHeaderHeight){
                    deltaY = refreshLayout.mHeaderHeight + scrollY
                }
                refreshLayout.scrollBy(0 , -deltaY)
                ViewCompat.postInvalidateOnAnimation(refreshLayout)
                mLastY = currY
            }else{
                mScroller.abortAnimation()
            }

//            val distance = mScroller.finalY - currY
//            val duration = mScroller.getDuration()- mScroller.timePassed()
//            val listView = refreshLayout.mContentView as ListView
//            smoothScrollBy(listView , mScroller.currVelocity.toInt(), distance, duration)
        }
    }

    //这里控制内容View的Fling 动作
    fun smoothScrollBy(scrollableView: View, velocityY: Int, distance: Int, duration: Int) {
        if (scrollableView is AbsListView) {
            val absListView = scrollableView
            if (Build.VERSION.SDK_INT >= 21) {
                absListView.fling(velocityY)
            } else {
                absListView.smoothScrollBy(distance, duration)
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
