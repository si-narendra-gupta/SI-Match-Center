package com.sportz.si_matchcenter.data.remote

import com.sportz.base.helper.BaseConfigContract
class MatchCenterConfig(val feedFixtureConfigContract: SiFeedFixtureConfigContract) :
    BaseConfigContract {
    override fun getBaseUrl(): String = feedFixtureConfigContract.getBaseUrl()
    override fun getIsDebugMode(): Boolean = feedFixtureConfigContract.getIsDebugMode()
    override fun getApiAuthKey(): String = ""
    override fun getPreferenceDataStoreName(): String = ""
}