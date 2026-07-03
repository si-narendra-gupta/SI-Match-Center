package com.sportz.si_matchcenter.data.remote

import com.sportz.base.business.domain.model.MenuItem
import com.sportz.base.helper.BaseConfigContract
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchCenterConfig @Inject constructor(val feedFixtureConfigContract: SiFeedFixtureConfigContract) :
    BaseConfigContract {
    override fun chromeToolbarColor(): Int = android.R.color.white
    override fun getBaseUrl(): String = feedFixtureConfigContract.getBaseUrl()
    override fun getIsDebugMode(): Boolean = feedFixtureConfigContract.getIsDebugMode()
    override fun getApiAuthKey(): String = ""
    override fun getPreferenceDataStoreName(): String = ""
    override fun getBottomMenuItems(): List<MenuItem>? = null
}