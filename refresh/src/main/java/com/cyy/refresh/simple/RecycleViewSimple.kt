package com.cyy.refresh.simple

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cyy.refresh.R
import com.cyy.refresh.refresh.RefreshLayout
import com.cyy.refresh.refresh.RefreshListener
import com.cyy.refresh.refresh.header.ProgressHeaderLayout
import kotlinx.android.synthetic.main.activity_recycle_view.*

class RecycleViewSimple : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle_view)

        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = Adapter()

        refreshLayout.refreshHeader = ProgressHeaderLayout()
        refreshLayout.mRefreshListener = object : RefreshListener{
            override fun onRefresh(refreshLayout: RefreshLayout) {
                refreshLayout.postDelayed({
                    refreshLayout.endRefresh()
                } , 2000)
            }

        }
    }

}
class Adapter : RecyclerView.Adapter<VH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        var view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_list_view, parent, false);
        return VH(view)
    }

    override fun getItemCount(): Int {
        return 30
    }

    override fun onBindViewHolder(holder: VH?, position: Int) {

    }


}

class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

}