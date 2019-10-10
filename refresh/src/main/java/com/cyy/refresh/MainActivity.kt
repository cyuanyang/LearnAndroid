package com.cyy.refresh

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

import com.cyy.refresh.simple.ListViewSimple
import com.cyy.refresh.simple.RecycleViewSimple
import com.cyy.refresh.simple.ScrollViewSimple


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listViewBtn.setOnClickListener { startActivity(Intent(this , ListViewSimple::class.java)) }

        scrollViewBtn.setOnClickListener { startActivity(Intent(this , ScrollViewSimple::class.java)) }

        recycleBtn.setOnClickListener { startActivity(Intent(this, RecycleViewSimple::class.java)) }
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
