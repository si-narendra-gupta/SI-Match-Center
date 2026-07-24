package com.sportz.simatchcenter

import android.app.Application
import com.sportz.si_matchcenter.MatchCenterSDK
import com.sportz.simatchcenter.data.remote.AppConfigMatchCenter

class SIMatchCenterApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MatchCenterSDK.init(AppConfigMatchCenter())
    }
}
