package com.cyy.refresh.refresh

import android.animation.ObjectAnimator
import android.content.Context
import android.support.v4.view.*
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.AbsListView
import android.widget.FrameLayout

/**
 * Created by cyy on 17/9/8.
 *
 */

internal inline fun log(msg:String){
    Log.i("RefreshLayout",msg)
}

internal fun ViewGroup.eachChildren(block:(position:Int, child: View)->Unit){
    for (i in (0..childCount-1)){
        block(i , getChildAt(i))
    }
}

enum class RefreshState{
    IDLE , //空闲状态
    REFRESHING , //正在处在刷新的状态
}

enum class ScrollState{
    IDEA ,
    DRAG ,
    FILING
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
    var animateInterval= 200L //动画的时间间隔

    private var headerView:View? = null
    var mContentView:View? = null
    private var mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop

    private val mScrollHelper:RefreshLayoutHelper = RefreshLayoutHelper(context)

    private var mInitDownY:Float = 0F //按下时的位置
    private var mInitLocation:Float = 0F //开始下拉时的手指位置
    private var mIsBeginDrag = false  //开始拖动 会拦截事件
    private var state:RefreshState = RefreshState.IDLE
    internal var mHeaderHeight = 0 //头部的高度


    var mScrollCallback:ScrollCallback? = null
    var mRefreshListener:RefreshListener? = null

    var scrollState:ScrollState = ScrollState.IDEA

    init { }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount != 1){
            throw IllegalStateException("RefreshLayout can host only one direct child")
        }

        mContentView = getChildAt(0)
    }

    private fun addHeaderLayout(refreshHeader:RefreshHeader){
        mHeaderHeight = refreshHeader.getHeaderHeight()
        
        headerView = refreshHeader.getHeaderView(this)
        if (headerView == null){
            throw IllegalStateException("需要先设置一个header")
        }
        var lp:LayoutParams? = headerView!!.layoutParams as? LayoutParams
        if (lp == null){
            mHeaderHeight = refreshHeader.getHeaderHeight()
            lp = LayoutParams(LayoutParams.MATCH_PARENT , mHeaderHeight)
        }
        lp.topMargin = -mHeaderHeight
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

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        var handle = false
        val action = ev.action
        mScrollHelper.initVelocityTrackerIfNotExists(ev)

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mInitDownY = ev.y
                mInitLocation = ev.y
                mScrollHelper.abortAnimationFinished()
            }
            MotionEvent.ACTION_UP -> {
                if (state == RefreshState.REFRESHING){
                    if (getScrollEffectiveDistance() < -mHeaderHeight){
                        smoothScrollTo(-mHeaderHeight)
                    }else{
                        //处在刷新的状态的时候 要处理fling 动作
                        scrollState = ScrollState.FILING
                        mScrollHelper.fling(0 , scrollY , 0 , 0 , -Int.MAX_VALUE , Int.MAX_VALUE )
                        ViewCompat.postInvalidateOnAnimation(this)
                        mScrollHelper.recycleVelocityTracker()
                    }
                }
            }
            MotionEvent.ACTION_CANCEL->{
                mScrollHelper.recycleVelocityTracker()
            }
        }
        /*
          当处在刷新的状态的时候，这个时候在滑动列表，应当将事件分发给自己
          当头部滑出可见区域后在将事件分发下去
        */
        if (state == RefreshState.REFRESHING
                && getScrollEffectiveDistance()<0){
            this.onTouchEvent(ev)
        }else{

        }
        // 走拦截事件
        super.dispatchTouchEvent(ev)
        return true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.action
        if (canChildScrollUp()) {
            log("还可以向上滑动" + ev)
            return false
        }

        log("到达顶部")

        when(action){
            MotionEvent.ACTION_DOWN -> {
                mInitDownY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                val y = ev.y
                val yDiff = y - mInitDownY
                log("yDiff = $yDiff .... $mTouchSlop")
                if (yDiff>mTouchSlop && !mIsBeginDrag){
                    mIsBeginDrag = true
                    mInitLocation = y
                }
            }
            MotionEvent.ACTION_UP ,
            MotionEvent.ACTION_CANCEL-> {
                mInitDownY = 0f
                mInitLocation = 0f
                mIsBeginDrag = false
            }
        }
        return mIsBeginDrag
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        when(action){
            MotionEvent.ACTION_DOWN -> { }
            MotionEvent.ACTION_MOVE -> {
                val y = event.y
                if (state == RefreshState.REFRESHING){
                    val offset = mInitLocation - y
                    scrollState = ScrollState.DRAG
                    refreshScroll(offset)
                }else{
                    val offset = (y-mInitLocation) * dragRate
                    offsetHeaderView(offset)
                }
                mInitLocation = y
            }
            MotionEvent.ACTION_UP -> {
                mInitLocation = 0f
                mIsBeginDrag = false
                if (state == RefreshState.REFRESHING){

                }else{
                    isIntoRefresh()
                }

            }
            MotionEvent.ACTION_CANCEL-> {
                scrollState = ScrollState.IDEA
            }
        }
        return true
    }

    override fun computeScroll() {
        mScrollHelper.computeScroll(this)
    }
    
    /**
     * 刷新时的滚动
     *
     * 刷新的时候header 和 ContentView 需要同时滚动
     *
     * scroll fling
     */
    private fun refreshScroll(offset:Float){

        log("refreshScroll offset=$offset")
        if (offset == 0F) return

        var realOffset = offset
        if (getScrollEffectiveDistance()<-mHeaderHeight){
            realOffset *= dragRate
        }
        log("refreshScroll realOffset=$realOffset")
        if (realOffset>0){
            scrollBy(0 , (realOffset+0.5).toInt())
        }else{
            scrollBy(0 , (realOffset-0.5).toInt())
        }

        log("refreshScroll getScrollEffectiveDistance=${getScrollEffectiveDistance()}")
    }

    private fun offsetHeaderView(offset:Float){
        if (offset == 0F)return
        val dis = getScrollEffectiveDistance()
        if (dis>=0){
            mIsBeginDrag = false
        }
        scrollBy(0 , -offset.toInt())
        dispatchHeaderScrollEvent(dis)

    }

    //真实的scrollY
    private fun realScrollY(y:Int):Int{
        var realY = y
        refreshHeader?.let {
            realY = if (realY < - mHeaderHeight){
                realY
            }else if (realY > 0){
                0
            }else{
                realY
            }
        }
        log("realY === $realY")
        return realY
    }

    override fun scrollTo(x: Int, y: Int) {
        super.scrollTo(x, realScrollY(y))
    }

    //是否进入刷新状态
    private fun isIntoRefresh() {
        refreshHeader?.let {
            val headerHeight = -mHeaderHeight
            val dis = getScrollEffectiveDistance()
            if (dis<=headerHeight){
                state = RefreshState.REFRESHING
                smoothScrollTo(headerHeight)
                mRefreshListener?.onRefresh(this)
            }else{
                state = RefreshState.IDLE
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
            scrollTo(0 , value)
        }
    }

    private fun reset(){
        scrollTo(0 , 0)
    }

    //内部的View是否还能往上滑动
    fun canChildScrollUp(): Boolean {
        return ViewCompat.canScrollVertically(mContentView, -1)
    }

    //获取有效的滑动距离
    fun getScrollEffectiveDistance():Int{
        return scrollY
    }

    private fun dispatchHeaderScrollEvent(dis:Int){
        log("dispatchHeaderScrollEvent>dis = $dis")
        mScrollCallback?.scroll(dis)
        refreshHeader?.onPulling(dis)
    }

    //结束滑动
    fun endRefresh(){
        state = RefreshState.IDLE
        log("结束刷新")
        smoothScrollTo(0)

    }
}

interface RefreshHeader{
    fun getHeaderView(refreshLayout:RefreshLayout):View  //返回header view
    fun getHeaderHeight():Int //header height
    fun onPulling(distance:Int) //下拉的过程
    fun onRefreshStateChanged(state:RefreshState)
}

