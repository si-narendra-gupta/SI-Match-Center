package com.sportz.base.helper

import com.sportz.base.business.domain.model.MenuItem

interface BaseConfigContract {
    fun chromeToolbarColor():Int
    fun getBaseUrl(): String
    fun getApiAuthKey(): String
    fun getPreferenceDataStoreName(): String
    fun getIsDebugMode(): Boolean
    fun getBottomMenuItems(): List<MenuItem>?
}
