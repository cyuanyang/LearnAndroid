package com.cyy.refresh.simple

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.cyy.refresh.R
import com.cyy.refresh.refresh.RefreshLayout
import com.cyy.refresh.refresh.RefreshListener
import com.cyy.refresh.refresh.header.RefreshHeaderLayout
import kotlinx.android.synthetic.main.activity_list_view_simple.*

class ScrollViewSimple : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_view_simple)

        refreshLayout.refreshHeader = RefreshHeaderLayout()

        refreshLayout.mRefreshListener = object : RefreshListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {

                refreshLayout.postDelayed({
                    refreshLayout.endRefresh()
                } , 2000)
            }
        }
    }
}
