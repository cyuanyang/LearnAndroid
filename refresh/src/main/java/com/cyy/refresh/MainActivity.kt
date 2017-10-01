package com.cyy.refresh

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.cyy.refresh.refresh.RefreshLayout
import com.cyy.refresh.refresh.RefreshListener
import com.cyy.refresh.refresh.header.RefreshHeaderLayout
import com.cyy.refresh.simple.ListViewSimple
import kotlinx.android.synthetic.main.activity_main.*
import android.view.animation.AnimationUtils
import android.view.animation.Animation
import android.R.attr.outAnimation
import android.view.animation.AnimationSet
import android.R.attr.inAnimation
import com.cyy.refresh.simple.LayoutAnimationsHideShow


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listViewBtn.setOnClickListener { startActivity(Intent(this , ListViewSimple::class.java)) }

        btn1.setOnClickListener {
            startActivity(Intent(this , LayoutAnimationsHideShow::class.java))
        }
//        refreshLayout.refreshHeader = RefreshHeaderLayout(this)
//
//        val count = 1000
//        var arr = arrayOfNulls<Int>(count)
//        for (i in (0 until count)){
//            arr[i] = i
//        }
//
//        var adapter = ArrayAdapter(this , android.R.layout.simple_list_item_1 , arr)
//        listView.adapter = adapter
//
//        refreshLayout.mRefreshListener = object : RefreshListener{
//            override fun onRefresh(refreshLayout: RefreshLayout) {
//
//                refreshLayout.postDelayed({
//                    refreshLayout.endRefresh()
//                } , 2000)
//            }
//        }
    }
}
