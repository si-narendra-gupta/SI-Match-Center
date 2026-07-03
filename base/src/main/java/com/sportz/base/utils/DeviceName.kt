package com.sportz.base.utils

import android.os.Build

class DeviceName {

    companion object {
        @JvmStatic
        fun getPhoneDeviceName():String {
            val model = Build.MANUFACTURER +" "+ Build.MODEL // returns model name
            return model;
        }

    }
}