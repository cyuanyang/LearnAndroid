package com.cyy.refresh.refresh.header

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.cyy.refresh.R
import com.cyy.refresh.refresh.RefreshHeader
import com.cyy.refresh.refresh.RefreshLayout
import com.cyy.refresh.refresh.RefreshState
import kotlinx.android.synthetic.main.header_default_layout.view.*

/**
 * Created by study on 17/9/8.
 *
 */


class RefreshHeaderLayout : RefreshHeader{
    override fun onPulling(distance: Int) {
    }

    override fun onRefreshStateChanged(state: RefreshState) {

    }

    override fun getHeaderView(refreshLayout: RefreshLayout): View {
        val headerView = LayoutInflater.from(refreshLayout.context)
                .inflate(R.layout.header_default_layout , refreshLayout , false)
        headerView.setBackgroundColor(Color.GREEN)
        return headerView
    }

    override fun getHeaderHeight(): Int {
        return 300
    }

}
