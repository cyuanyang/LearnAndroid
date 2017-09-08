package com.cyy.refresh

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.cyy.refresh.refresh.header.RefreshHeaderLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        refreshLayout.refreshHeader = RefreshHeaderLayout(this)

        var arr = arrayOf("1","2","3","4","1","2","3","4","1","2","3","4","1","2","3","4")
        var adapter = ArrayAdapter(this , android.R.layout.simple_list_item_1 , arr)
        listView.adapter = adapter
    }
}
