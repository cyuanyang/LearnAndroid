package com.cyy.refresh.refresh.header

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.cyy.refresh.R
import com.cyy.refresh.refresh.RefreshHeader
import kotlinx.android.synthetic.main.header_default_layout.view.*

/**
 * Created by study on 17/9/8.
 *
 */

class RefreshHeaderLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int
) : LinearLayout(context, attrs, defStyleAttr) , RefreshHeader{

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {

        orientation = HORIZONTAL
        LayoutInflater.from(context).inflate(R.layout.header_default_layout , this)
        progressBar.progress = 50
    }

    override fun getHeaderView(): View {
        setBackgroundColor(Color.GREEN)
        return this
    }

    override fun getHeaderHeight(): Int {
        return 300
    }

}
