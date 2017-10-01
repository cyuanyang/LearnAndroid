package com.cyy.refresh.refresh

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ListView

/**
 * Created by study on 17/9/15.
 *
 */

class MyListView@JvmOverloads constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int
) : ListView(context, attrs, defStyleAttr){

    constructor(context: Context) : this(context , null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs , 0)


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        log("MyListView dispatchTouchEvent－－－－ $ev")
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        log("MyListView onInterceptTouchEvent－－－－ $ev")
        return super.onInterceptTouchEvent(ev)
    }

    override fun setOverScrollMode(mode: Int) {
        super.setOverScrollMode(mode)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        log("MyListView onTouchEvent－－－－ $ev")
        return super.onTouchEvent(ev)
    }
}