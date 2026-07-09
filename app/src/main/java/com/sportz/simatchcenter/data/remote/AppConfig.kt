package com.sportz.simatchcenter.data.remote

import com.sportz.si_matchcenter.data.remote.SiFeedFixtureConfigContract
import com.sportz.simatchcenter.BuildConfig


object AppReplaceKeys {
    const val DATA_IMAGE_VERSION = "{data_image_version}"
    const val TEAM_ID = "{team_id}"
    const val GAME_ID = "{game_id}"
    const val MATCH_ID = "{match_id}"
    const val INNING = "{inning}"

}
class AppConfig : SiFeedFixtureConfigContract {
    override fun getBaseUrl(): String = "https://www.rajasthanroyals.com/" // Example URL

    override fun getIsDebugMode(): Boolean = BuildConfig.DEBUG

    override fun getTeamLogo(teamId: String): String {
        return getBaseUrl() + "static-assets/images/teams/{team_id}.png?v={data_image_version}".replace(
            AppReplaceKeys.TEAM_ID, teamId,
        ).replace(
            oldValue = AppReplaceKeys.DATA_IMAGE_VERSION, newValue = "6.21"
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

    override fun getBallByBallUrl(matchId: String, inning: Int): String {
        return getBaseUrl() + "functions/wallstream/?sport_id=1&client_id=PO30XPfgTIePIt3A8JFsgg==&match_id={match_id}&page_size=0&page_no=0&session={inning}".replace(
            AppReplaceKeys.MATCH_ID, matchId
        ).replace(AppReplaceKeys.INNING, inning.toString())
    }

    override fun getManhattanUrl(gameId: String, inning: Int): String {
        return getBaseUrl() + "cricket/live/json/{game_id}_overbyover_{inning}.json".replace(
            "{game_id}", gameId
        ).replace(AppReplaceKeys.INNING, inning.toString())
    }

    override fun getSpiderUrl(gameId: String, inning: Int): String {
        return getBaseUrl() + "cricket/live/json/{game_id}_batsman_splits_{inning}.json".replace(
            "{game_id}", gameId
        ).replace(AppReplaceKeys.INNING, inning.toString())
    }

    /*override fun getMatchTabs() = try {
        GsonBuilder().create().fromJson(
            "{\"tab_list\":{\"upcoming\":[{\"id\":\"commentary\",\"order\":1,\"title\":\"Commentary\",\"visible\":true,\"interaction\":true,\"selected\":false},{\"id\":\"teams\",\"order\":2,\"title\":\"Teams\",\"visible\":true,\"interaction\":true,\"selected\":false},{\"id\":\"match_info\",\"order\":3,\"title\":\"Match Info\",\"visible\":true,\"interaction\":true,\"selected\":true}],\"live\":[{\"id\":\"commentary\",\"order\":1,\"title\":\"Commentary\",\"visible\":true,\"interaction\":true,\"selected\":false},{\"id\":\"scorecard\",\"order\":2,\"title\":\"Scorecard\",\"visible\":true,\"interaction\":true,\"selected\":true},{\"id\":\"teams\",\"order\":3,\"title\":\"Teams\",\"visible\":true,\"interaction\":true,\"selected\":false},{\"id\":\"graphs\",\"order\":4,\"title\":\"Graphs\",\"visible\":true,\"interaction\":true,\"selected\":false,\"sub_tabs\":[{\"id\":\"manhattan\",\"order\":1,\"title\":\"Manhattan\",\"visible\":true,\"interaction\":true,\"selected\":true},{\"id\":\"innings_progression\",\"order\":2,\"title\":\"Innings Progression\",\"visible\":true,\"interaction\":true,\"selected\":false},{\"id\":\"run_rate\",\"order\":3,\"title\":\"Run Rate\",\"visible\":false,\"interaction\":true,\"selected\":false},{\"id\":\"spider\",\"order\":4,\"title\":\"Spider\",\"visible\":true,\"interaction\":true,\"selected\":false},{\"id\":\"wagon_wheel\",\"order\":5,\"title\":\"Wagon Wheel\",\"visible\":false,\"interaction\":true,\"selected\":false}]},{\"id\":\"match_info\",\"order\":5,\"title\":\"Match Info\",\"visible\":true,\"interaction\":true,\"selected\":false}],\"result\":[{\"id\":\"commentary\",\"order\":1,\"title\":\"Commentary\",\"visible\":true,\"interaction\":true,\"selected\":false},{\"id\":\"scorecard\",\"order\":2,\"title\":\"Scorecard\",\"visible\":true,\"interaction\":true,\"selected\":true},{\"id\":\"teams\",\"order\":3,\"title\":\"Teams\",\"visible\":true,\"interaction\":true,\"selected\":false},{\"id\":\"graphs\",\"order\":4,\"title\":\"Graphs\",\"visible\":true,\"interaction\":true,\"selected\":false,\"sub_tabs\":[{\"id\":\"manhattan\",\"order\":1,\"title\":\"Manhattan\",\"visible\":true,\"interaction\":true,\"selected\":true},{\"id\":\"innings_progression\",\"order\":2,\"title\":\"Innings Progression\",\"visible\":true,\"interaction\":true,\"selected\":false},{\"id\":\"run_rate\",\"order\":3,\"title\":\"Run Rate\",\"visible\":false,\"interaction\":true,\"selected\":false},{\"id\":\"spider\",\"order\":4,\"title\":\"Spider\",\"visible\":true,\"interaction\":true,\"selected\":false},{\"id\":\"wagon_wheel\",\"order\":5,\"title\":\"Wagon Wheel\",\"visible\":false,\"interaction\":true,\"selected\":false}]},{\"id\":\"match_info\",\"order\":5,\"title\":\"Match Info\",\"visible\":true,\"interaction\":true,\"selected\":false}]}}",
            MatchTabList::class.java
        )
    } catch (e: Exception) {
        null
    }*/

}