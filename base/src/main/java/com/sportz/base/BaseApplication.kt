package com.sportz.base

import android.app.Application
import com.sportz.base.helper.BaseConfigContract
import javax.inject.Inject


abstract class BaseApplication : Application() {

    @Inject
    lateinit var baseConfigContract: BaseConfigContract


    override fun onCreate() {
        super.onCreate()

    }
}
