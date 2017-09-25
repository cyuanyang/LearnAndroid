package com.cyy.refresh.refresh

import android.util.TypedValue
import android.view.View

/**
 * Created by study on 17/9/25.
 *
 */

internal fun View.dp2px(dp:Int):Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP , dp.toFloat() ,resources.displayMetrics)