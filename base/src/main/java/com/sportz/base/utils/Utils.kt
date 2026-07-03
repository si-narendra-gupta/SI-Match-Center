package com.sportz.base.utils

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable

inline fun <T1 : Any, T2 : Any, R : Any> safeLet(
    p1: T1?,
    p2: T2?,
    block: (T1, T2) -> R?,
): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

fun String.isStringEmpty(): String? {
    return if (isNullOrEmpty()) null
    else this
}

inline fun <reified T : Parcelable> Bundle.parcelableArrayList(key: String): List<T>? = when {
    SDK_INT >= 33 -> getParcelableArrayList(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableArrayList<T>(key)
}