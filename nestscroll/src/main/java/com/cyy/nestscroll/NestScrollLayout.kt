package com.cyy.nestscroll

import android.content.Context
import android.support.v4.view.NestedScrollingParent
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.support.v4.view.ViewCompat.canScrollVertically



/**
 * Created by study on 17/9/12.
 *
 */

class NestScrollLayout:LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        isNestedScrollingEnabled = true
    }



    override fun onStartNestedScroll(child: View, target: View, axes: Int): Boolean {
        return true
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        Log.e("ddd" , "fffff")

    }

    override fun onNestedPreScroll(target: View?, dx: Int, dy: Int, consumed: IntArray) {
        super.onNestedPreScroll(target, dx, dy, consumed)
        val hiddenTop = dy > 0 && scrollY < 150
        val showTop = dy < 0 && scrollY > 0 && !ViewCompat.canScrollVertically(target, -1)

        if (hiddenTop || showTop) {
            scrollBy(0, dy)
            consumed[1] = dy
        }
    }


}