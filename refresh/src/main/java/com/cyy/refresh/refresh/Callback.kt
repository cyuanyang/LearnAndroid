package com.cyy.refresh.refresh

/**
 * Created by study on 17/9/8.
 *
 */

interface RefreshListener{
    fun onRefresh(refreshLayout: RefreshLayout)
}

interface ScrollCallback{
    fun scroll(dis:Int) //滑动回调 dis 滑动的距离
}

