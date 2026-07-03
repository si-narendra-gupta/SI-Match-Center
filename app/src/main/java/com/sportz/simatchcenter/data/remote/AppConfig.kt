package com.sportz.simatchcenter.data.remote

import com.google.gson.Gson
import com.sportz.base.business.domain.model.MenuItem
import com.sportz.base.helper.BaseConfigContract
import com.sportz.si_matchcenter.business.domain.model.MatchTabList
import com.sportz.si_matchcenter.data.remote.SiFeedFixtureConfigContract
import com.sportz.simatchcenter.BuildConfig
import com.sportz.simatchcenter.R
import com.sportz.simatchcenter.helper.AppReplaceKeys
import com.sportz.simatchcenter.helper.AppReplaceKeys.DATA_IMAGE_VERSION
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppConfig @Inject constructor(val gson: Gson) : BaseConfigContract,
    SiFeedFixtureConfigContract {
    override fun appName(): Int = R.string.app_name
    override fun chromeToolbarColor(): Int = android.R.color.white // Default or specific color
    override fun getBaseUrl(): String = "https://stg-rr.sportz.io/" // Example URL
    override fun getApiAuthKey(): String = ""
    override fun getPreferenceDataStoreName(): String = "si_match_center_prefs"
    override fun getCaptchaSiteKey(): String = ""
    override fun getIsDebugMode(): Boolean = BuildConfig.DEBUG
    override fun getBottomMenuItems(): List<MenuItem>? = null

    override fun getTeamLogo(teamId: String): String {
        return getBaseUrl() + "static-assets/images/teams/{team_id}.png?v={data_image_version}".replace(
            AppReplaceKeys.TEAM_ID, teamId,
        ).replace(
            DATA_IMAGE_VERSION, "6.21"
        )
    }

    override fun getCurrentTeamId(): String {
        return "1110"
    }

    override fun getMatchCenterUrl(gameId: String): String {
        return getBaseUrl() + "cricket/live/json/{game_id}.json".replace(
            AppReplaceKeys.GAME_ID, gameId
        )
    }

    //https://www.rajasthanroyals.com/functions/wallstream/?sport_id=1&client_id=PO30XPfgTIePIt3A8JFsgg==&match_id=270670&page_size=0&page_no=0&session=6

    override fun getBallByBallUrl(matchId: String, inning: Int): String {
        return getBaseUrl() + "functions/wallstream/?sport_id=1&client_id=PO30XPfgTIePIt3A8JFsgg==&match_id={match_id}&page_size=0&page_no=0&session={inning}".replace(
            AppReplaceKeys.MATCH_ID, matchId
        ).replace(AppReplaceKeys.INNING, inning.toString())
    }

    override fun getManhattanUrl(gameId: String, inning: Int): String {
        return "https://www.rajasthanroyals.com/cricket/live/json/{game_id}_overbyover_{inning}.json".replace(
                "{game_id}",
                gameId
            ).replace("{inning}", inning.toString())
    }

    override fun getSpiderUrl(gameId: String, inning: Int): String {
        return "https://www.rajasthanroyals.com/cricket/live/json/{game_id}_batsman_splits_{inning}.json".replace(
                "{game_id}",
                gameId
            ).replace("{inning}", inning.toString())
    }

    /*override fun getMatchTabs() = try {
        gson.fromJson(
            "{\"tab_list\":{\"upcoming\":[{\"id\":\"commentary\",\"order\":1,\"title\":\"Commentary\",\"visible\":true,\"interaction\":true,\"selected\":false},{\"id\":\"teams\",\"order\":2,\"title\":\"Teams\",\"visible\":true,\"interaction\":true,\"selected\":false},{\"id\":\"match_info\",\"order\":3,\"title\":\"Match Info\",\"visible\":true,\"interaction\":true,\"selected\":true}],\"live\":[{\"id\":\"commentary\",\"order\":1,\"title\":\"Commentary\",\"visible\":true,\"interaction\":true,\"selected\":false},{\"id\":\"scorecard\",\"order\":2,\"title\":\"Scorecard\",\"visible\":true,\"interaction\":true,\"selected\":true},{\"id\":\"teams\",\"order\":3,\"title\":\"Teams\",\"visible\":true,\"interaction\":true,\"selected\":false},{\"id\":\"graphs\",\"order\":4,\"title\":\"Graphs\",\"visible\":true,\"interaction\":true,\"selected\":false,\"sub_tabs\":[{\"id\":\"manhattan\",\"order\":1,\"title\":\"Manhattan\",\"visible\":true,\"interaction\":true,\"selected\":true},{\"id\":\"innings_progression\",\"order\":2,\"title\":\"Innings Progression\",\"visible\":true,\"interaction\":true,\"selected\":false},{\"id\":\"run_rate\",\"order\":3,\"title\":\"Run Rate\",\"visible\":false,\"interaction\":true,\"selected\":false},{\"id\":\"spider\",\"order\":4,\"title\":\"Spider\",\"visible\":true,\"interaction\":true,\"selected\":false},{\"id\":\"wagon_wheel\",\"order\":5,\"title\":\"Wagon Wheel\",\"visible\":false,\"interaction\":true,\"selected\":false}]},{\"id\":\"match_info\",\"order\":5,\"title\":\"Match Info\",\"visible\":true,\"interaction\":true,\"selected\":false}],\"result\":[{\"id\":\"commentary\",\"order\":1,\"title\":\"Commentary\",\"visible\":true,\"interaction\":true,\"selected\":false},{\"id\":\"scorecard\",\"order\":2,\"title\":\"Scorecard\",\"visible\":true,\"interaction\":true,\"selected\":true},{\"id\":\"teams\",\"order\":3,\"title\":\"Teams\",\"visible\":true,\"interaction\":true,\"selected\":false},{\"id\":\"graphs\",\"order\":4,\"title\":\"Graphs\",\"visible\":true,\"interaction\":true,\"selected\":false,\"sub_tabs\":[{\"id\":\"manhattan\",\"order\":1,\"title\":\"Manhattan\",\"visible\":true,\"interaction\":true,\"selected\":true},{\"id\":\"innings_progression\",\"order\":2,\"title\":\"Innings Progression\",\"visible\":true,\"interaction\":true,\"selected\":false},{\"id\":\"run_rate\",\"order\":3,\"title\":\"Run Rate\",\"visible\":false,\"interaction\":true,\"selected\":false},{\"id\":\"spider\",\"order\":4,\"title\":\"Spider\",\"visible\":true,\"interaction\":true,\"selected\":false},{\"id\":\"wagon_wheel\",\"order\":5,\"title\":\"Wagon Wheel\",\"visible\":false,\"interaction\":true,\"selected\":false}]},{\"id\":\"match_info\",\"order\":5,\"title\":\"Match Info\",\"visible\":true,\"interaction\":true,\"selected\":false}]}}",
            MatchTabList::class.java
        )
    } catch (e: Exception) {
        null
    }*/

}