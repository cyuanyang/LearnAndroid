package com.cyy.loading.loading

import android.util.Log
import android.util.TypedValue
import android.view.View

/**
 * Created by cyy on 17/9/25.
 *
 */

internal fun View.dp2px(dp:Int):Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics)

internal inline fun log(msg:String){
    Log.i("loadingProgress" , msg)
}