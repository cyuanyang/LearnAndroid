package com.cyy.learnglide

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.RelativeLayout
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView.adapter = MyAdapter()

        Log.e("tag" , "cyy = $cyy")

    }
}

val Activity.cyy:Int
    get() = 1111


class  MyAdapter : BaseAdapter(){

    val imgs = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1503316334076&di=6f69577ba79c005d30535362096a830b&imgtype=0&src=http%3A%2F%2Fpic2.ooopic.com%2F12%2F13%2F96%2F40bOOOPIC8a_1024.jpg"

    var list:MutableList<String> = mutableListOf(
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1503316334076&di=3351164c8f046f795b6b5f29caa3e30f&imgtype=0&src=http%3A%2F%2Fimg2.3lian.com%2F2014%2Ff2%2F110%2Fd%2F56.jpg",
            imgs,
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1503320506604&di=697bbc9fe2df7e91a98064af46b0dc65&imgtype=jpg&src=http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D547578705%2C4237254642%26fm%3D214%26gp%3D0.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1503320517636&di=7b870f007ec521867311c9c304b15180&imgtype=jpg&src=http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D3292333560%2C2877577876%26fm%3D214%26gp%3D0.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1503316334075&di=c234b239d42b3fc78befdbe3ac014806&imgtype=0&src=http%3A%2F%2Foss.gkstk.com%2Fimages%2F2017%2F2%2F23185618358.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1503316334075&di=a072f08263818989c1d94d14527d0ff3&imgtype=0&src=http%3A%2F%2Ftupian.enterdesk.com%2F2013%2Fxll%2F012%2F26%2F3%2F7.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1503316334075&di=4f74f8a0a5caedfec17b6d73f131d3d8&imgtype=0&src=http%3A%2F%2Fpic31.nipic.com%2F20130718%2F12834382_112335424179_2.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1503316334075&di=94501ca470642068df23f1dd55ac0612&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3Da968c362942f07084b082243814dd2ec%2F29381f30e924b89987bfa81564061d950a7bf618.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1503316334075&di=91f293f10d83895e2560c51219fc739b&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3Da90140c0a9cc7cd9ee203c9a51684b4a%2F8c1001e93901213fbda015f15ee736d12f2e950f.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1503316334074&di=b9a2fb90d9dd3bbdca4badb18560f5be&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D48a081e4888ba61ecbe3c06c295dfd7f%2Fa9d3fd1f4134970ac9db97019fcad1c8a7865d70.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1503316334074&di=003d100367f94af6107f0db7edfb89f7&imgtype=0&src=http%3A%2F%2Fseopic.58pic.com%2Fphoto%2F00000%2F0619.jpg_wh1200.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1503316334074&di=28147043b60e9292cfcc18bfc9e6544b&imgtype=0&src=http%3A%2F%2Fpic.jy135.com%2Fallimg%2F20170704%2F13-1FF4112006.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1503316334074&di=ef77cf6a4b0612f3cb12c16c80989734&imgtype=0&src=http%3A%2F%2Farticle.fd.zol-img.com.cn%2Ft_s640x2000%2Fg4%2FM08%2F0A%2F0A%2FCg-4WlPMw_6Ieol4AAFX1gsVtiwAAPyAQA4vM0AAVfu669.jpg"
    )

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image , parent , false)
        var holder = MyViewHolder(view)
//        GlideApp.with(parent.context)
//                .load(list[position])
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
//                .into(imageView)
        return view
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return  position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

    class MyViewHolder(override val containerView: View):RecyclerView.ViewHolder(containerView) ,LayoutContainer{
        fun bind(){
        }
    }

}
