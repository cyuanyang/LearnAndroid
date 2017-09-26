package com.cyy.refresh.simple

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.cyy.refresh.R
import com.cyy.refresh.refresh.RefreshLayout
import com.cyy.refresh.refresh.RefreshListener
import com.cyy.refresh.refresh.header.RefreshHeaderLayout
import kotlinx.android.synthetic.main.activity_list_view_simple.*
import kotlinx.android.synthetic.main.item_list_view.view.*

class ListViewSimple : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view_simple)
        refreshLayout.refreshHeader = RefreshHeaderLayout()
        val datas = ( 0 until 1000).map { Model("cyy--$it" , it) }
        listView.adapter = MyAdapter(datas)
//        refreshLayout.mRefreshListener = object : RefreshListener {
//            override fun onRefresh(refreshLayout: RefreshLayout) {
//
//                refreshLayout.postDelayed({
//                    refreshLayout.endRefresh()
//                } , 2000)
//            }
//        }
    }
}

class MyAdapter(datas:List<Model>) : BaseAdapter(){

    var datas:List<Model> = datas

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var cv = convertView
        if (convertView==null){
            cv = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_view , parent , false)

        }

        cv!!.nameView.text = datas[position].name
        cv.indexView.text = datas[position].num.toString()

        return cv
    }

    override fun getItem(position: Int): Any {
        return datas[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return datas.count()
    }

}
