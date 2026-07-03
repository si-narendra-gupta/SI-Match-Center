package com.sportz.simatchcenter.data.remote

import com.sportz.base.business.domain.model.MenuItem
import com.sportz.base.helper.BaseConfigContract
import com.sportz.simatchcenter.BuildConfig
import com.sportz.simatchcenter.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppConfig @Inject constructor() : BaseConfigContract {
    override fun appName(): Int = R.string.app_name
    override fun chromeToolbarColor(): Int = android.R.color.white // Default or specific color
    override fun getBaseUrl(): String = "https://api.example.com/" // Example URL
    override fun getApiAuthKey(): String = "your_api_key_here"
    override fun getPreferenceDataStoreName(): String = "si_match_center_prefs"
    override fun getCaptchaSiteKey(): String = ""
    override fun getIsDebugMode(): Boolean = BuildConfig.DEBUG
    override fun getBottomMenuItems(): List<MenuItem>? = null
}