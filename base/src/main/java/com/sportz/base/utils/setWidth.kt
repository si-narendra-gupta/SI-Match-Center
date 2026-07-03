package com.sportz.base.utils

import android.view.View
import android.view.ViewGroup

fun View.setWidth(widthInPixel: Int) {
    val tempLayoutParams = layoutParams as ViewGroup.LayoutParams
    tempLayoutParams.width = widthInPixel
    layoutParams = tempLayoutParams
}