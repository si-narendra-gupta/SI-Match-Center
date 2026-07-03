package com.sportz.si_matchcenter.presentation.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.sportz.base.helper.BaseConfigContract
import com.sportz.base.helper.Result
import com.sportz.base.ui.common.BaseScreenViewModel
import com.sportz.base.ui.common.MviIntent
import com.sportz.base.ui.common.UiState
import com.sportz.si_matchcenter.business.domain.model.EventState
import com.sportz.si_matchcenter.business.domain.model.IPLMatch
import com.sportz.si_matchcenter.business.domain.model.MatchTabId
import com.sportz.si_matchcenter.business.domain.model.MatchTabItem
import com.sportz.si_matchcenter.business.domain.model.OverData
import com.sportz.si_matchcenter.business.domain.model.SpiderZone
import com.sportz.si_matchcenter.business.domain.model.themecolor.MatchCenterThemeColors
import com.sportz.si_matchcenter.business.interactor.GetManhattanDataUseCase
import com.sportz.si_matchcenter.business.interactor.GetMatchListingUseCase
import com.sportz.si_matchcenter.business.interactor.GetSpiderGraphUseCase
import com.sportz.si_matchcenter.business.interactor.GetWallStreamUseCase
import com.sportz.si_matchcenter.data.remote.SiFeedFixtureConfigContract
import com.sportz.si_matchcenter.helper.ThemeHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class MatchCenterUiState(
    val matches: IPLMatch? = null,
    val tabItems: List<MatchTabItem> = emptyList(),
    val selectedTab: MatchTabItem? = null,
    val manhattanData: List<OverData> = emptyList(),
    val spiderData: SpiderZone? = null,
    val isManhattanLoading: Boolean = false,
    val isSpiderLoading: Boolean = false,
    val selectedInning: Int = 1,
    val scorecardTeamIndex: Int = 0,
    val teamsTabTeamIndex: Int = 0,
    val selectedSubTabId: String? = null,
    val totalPlayedInnings: Int = 0,
    val themeColors: MatchCenterThemeColors? = null
) : UiState

sealed class MatchCenterIntent : MviIntent {
    data class LoadMatches(val gameId: String) : MatchCenterIntent()
    data class LoadManhattanData(val gameId: String) : MatchCenterIntent()
    data class LoadCommentary(val matchId: String, val eventState: EventState, val inning: Int) :
        MatchCenterIntent()
}

@HiltViewModel
class MatchCenterViewModel @Inject constructor(
    private val baseConfigContract: BaseConfigContract,
    private val fixtureConfigContract: SiFeedFixtureConfigContract,
    private val getMatchListingUseCase: GetMatchListingUseCase,
    private val getManhattanDataUseCase: GetManhattanDataUseCase,
    private val getSpiderGraphUseCase: GetSpiderGraphUseCase,
    private val getWallStreamUseCase: GetWallStreamUseCase,
    private val gson: Gson
) : BaseScreenViewModel<MatchCenterUiState, MatchCenterIntent>(baseConfigContract) {


    // for dynamic
    /*init {
        updateSuccessData(MatchCenterUiState(themeColors = fixtureConfigContract.getMatchCenterThemeColors()))
    }*/

    fun setTheme(themeJson: String?) {
        val theme = ThemeHelper.getMatchCenterThemeColors(themeJson, gson)
        val currentState = getCurrentUiData() ?: MatchCenterUiState()
        updateSuccessData(currentState.copy(themeColors = theme))
    }

    fun selectedTab(value: MatchTabItem?) {
        val currentState = getCurrentUiData() ?: MatchCenterUiState()
        val defaultSubTabId = if (value?.tabId == MatchTabId.GRAPHS) {
            value.sub_tabs?.firstOrNull { it.selected }?.id ?: value.sub_tabs?.firstOrNull()?.id
        } else {
            currentState.selectedSubTabId
        }
        val newState = currentState.copy(selectedTab = value, selectedSubTabId = defaultSubTabId)
        updateSuccessData(newState)
        refreshDataForSelectedTab(newState)
    }

    fun selectedSubTab(id: String?) {
        val currentState = getCurrentUiData() ?: MatchCenterUiState()
        if (currentState.selectedSubTabId == id) return
        val newState = currentState.copy(selectedSubTabId = id)
        updateSuccessData(newState)
        refreshDataForSelectedTab(newState)
    }

    fun onScorecardTeamSelected(index: Int) {
        val currentState = getCurrentUiData() ?: MatchCenterUiState()
        updateSuccessData(currentState.copy(scorecardTeamIndex = index))
    }

    fun onTeamsTabTeamSelected(index: Int) {
        val currentState = getCurrentUiData() ?: MatchCenterUiState()
        updateSuccessData(currentState.copy(teamsTabTeamIndex = index))
    }

    private fun refreshDataForSelectedTab(state: MatchCenterUiState, showLoading: Boolean = true) {
        val gameId = state.matches?.matchGameId ?: return
        when (state.selectedTab?.tabId) {
            MatchTabId.GRAPHS -> {
                // Manhattan is usually the default, or we can fetch it if its sub-tab is active
                // For now, keeping Manhattan fetch as is, or making it conditional if needed.
                // The user specifically asked for Spider conditional fetch.
                if (state.selectedSubTabId == "innings_progression" || state.selectedSubTabId == "manhattan") fetchManhattanData(
                    gameId,
                    showLoading
                )

                if (state.selectedSubTabId == "spider") {
                    fetchSpiderData(gameId, inning = state.totalPlayedInnings, showLoading)
                }
            }

            else -> {}
        }
    }

    fun fetchSpiderData(gameId: String, inning: Int, showLoading: Boolean = true) {

        val currentState = getCurrentUiData() ?: MatchCenterUiState()

        if (showLoading) {
            updateSuccessData(currentState.copy(isSpiderLoading = true, selectedInning = inning, spiderData = null))
        }

        viewModelScope.launch {
            when (val result = getSpiderGraphUseCase(gameId, inning)) {
                is Result.Success -> {
                    val latestState = getCurrentUiData()
                    if (latestState?.selectedInning == inning) {
                        updateSuccessData(
                            latestState.copy(
                                spiderData = result.data, isSpiderLoading = false
                            )
                        )
                    }
                }

                else -> {
                    updateSuccessData(
                        getCurrentUiData()?.copy(
                            isSpiderLoading = false
                        ) ?: currentState
                    )
                }
            }
        }
    }

    private var currentGameId: String? = null
    private var pollingJob: Job? = null

    override fun handleIntent(intent: MatchCenterIntent) {
        when (intent) {
            is MatchCenterIntent.LoadMatches -> {
                currentGameId = intent.gameId
                fetchMatches(intent.gameId)
            }

            is MatchCenterIntent.LoadManhattanData -> {
                fetchManhattanData(intent.gameId)
            }

            is MatchCenterIntent.LoadCommentary -> {
                fetchCommentary(intent.matchId, intent.eventState, intent.inning)
            }
        }
    }

    private fun fetchCommentary(matchId: String, eventState: EventState, inning: Int) {
        viewModelScope.launch {
            val result = getWallStreamUseCase(matchId, eventState, inning)
            if (result is Result.Success) {
                val currentState = getCurrentUiData() ?: MatchCenterUiState()
                updateSuccessData(
                    currentState.copy(
                        matches = currentState.matches?.copy(customMetaData = result.data),
                        selectedInning = inning,
                        totalPlayedInnings = currentState.matches?.playedInnings ?: 0
                    )
                )
            }
        }
    }

    private fun fetchManhattanData(gameId: String, showLoading: Boolean = true) {
        val currentState = getCurrentUiData() ?: MatchCenterUiState()
        if (showLoading) {
            updateSuccessData(currentState.copy(isManhattanLoading = true))
        }

        viewModelScope.launch {
            val totalInnings = currentState.matches?.totalInnings ?: 1
            val inning1Deferred = async { getManhattanDataUseCase(gameId, 1) }
            val inning2Deferred = if (totalInnings >= 2) async { getManhattanDataUseCase(gameId, 2) } else null

            val res1 = inning1Deferred.await()
            val res2 = inning2Deferred?.await()

            if (res1 is Result.Success && (res2 == null || res2 is Result.Success)) {
                val data1 = res1.data.overByOver ?: emptyList()
                val data2 = res2?.data?.overByOver ?: emptyList()

                val maxOvers = maxOf(data1.size, data2.size)
                val mergedData = (1..maxOvers).map { overNum ->
                    val o1 = data1.find { it.over == overNum }
                    val o2 = data2.find { it.over == overNum }
                    OverData(
                        overNumber = overNum,
                        team1Runs = o1?.runs?.toIntOrNull() ?: 0,
                        team1Wickets = o1?.wickets?.toIntOrNull() ?: 0,
                        team2Runs = o2?.runs?.toIntOrNull() ?: 0,
                        team2Wickets = o2?.wickets?.toIntOrNull() ?: 0,
                        isTeam1Over = o1 != null,
                        isTeam2Over = o2 != null
                    )
                }
                updateSuccessData(
                    getCurrentUiData()?.copy(
                        manhattanData = mergedData,
                        isManhattanLoading = false
                    ) ?: MatchCenterUiState(manhattanData = mergedData, isManhattanLoading = false)
                )
            } else {
                updateSuccessData(getCurrentUiData()?.copy(isManhattanLoading = false) ?: currentState)
            }
        }
    }

    private fun fetchMatches(gameID: String) {
        val currentState = getCurrentUiData()
        val isNewMatch = currentState?.matches?.matchGameId != gameID && currentState?.matches?.matchId != gameID

        viewModelScope.launch {
            try {
                executeWithLoading(
                    onLoading = { if (isNewMatch) setLoading() },
                    onSuccess = { matches ->
                        if (matches.eventState == EventState.LIVE) {
                            startPolling(gameID)
                        } else {
                            stopPolling()
                        }
                        val tabs = getTabsForState(matches.eventState)
                        val defaultTab =
                            if (isNewMatch || matches.eventState != currentState?.matches?.eventState) {
                                tabs.firstOrNull { it.selected == true } ?: tabs.firstOrNull()
                            } else {
                                currentState?.selectedTab ?: tabs.firstOrNull { it.selected == true }
                                ?: tabs.firstOrNull()
                            }

                        val baseState = if (isNewMatch) {
                            MatchCenterUiState(themeColors = currentState?.themeColors)
                        } else {
                            currentState ?: MatchCenterUiState()
                        }

                        val newState = baseState.copy(
                            matches = matches,
                            tabItems = tabs,
                            selectedTab = defaultTab,
                            selectedInning = matches.innings?.lastOrNull()?.apiInningIndex ?: 1,
                            totalPlayedInnings = matches.playedInnings,
                            scorecardTeamIndex = matches.participants?.indexOfLast { it.highlight == true }?.coerceAtLeast(0) ?: 0,
                            teamsTabTeamIndex = matches.participants?.indexOfLast { it.highlight == true }?.coerceAtLeast(0) ?: 0
                        )
                        updateSuccessData(newState)

                        // Optimization: Pre-fetch Graph data in background if not already on Graphs tab
                        /*if (newState.selectedTab?.tabId != MatchTabId.GRAPHS) {
                            preFetchGraphData(matches.matchGameId ?: "", newState.selectedInning)
                        }*/
                    },
                    block = {
                        val result = getMatchListingUseCase(gameID)
                        if (result is Result.Success) {
                            result.data
                        } else {
                            throw (result as Result.Error).exception
                        }
                    })
            } catch (t: Throwable) {
                setError(t.message ?: "Unknown error", t)
            }
        }
    }

    private fun preFetchGraphData(gameId: String, inning: Int) {
        // Manhattan pre-fetch
        fetchManhattanData(gameId, showLoading = false)
        // Spider pre-fetch
        fetchSpiderData(gameId, inning, showLoading = false)
    }

    private fun getTabsForState(state: EventState): List<MatchTabItem> {

        val tabs = fixtureConfigContract.getMatchTabs()?.tab_list
        val configuredTabs = when (state) {
            EventState.LIVE -> tabs?.live
            EventState.RESULT -> tabs?.result
            EventState.UPCOMING -> tabs?.upcoming
        }?.filterNotNull()?.filter { it.visible == true }?.sortedBy { it.order }

        return if (configuredTabs.isNullOrEmpty()) {
            MatchTabItem.getDefaultTabsForState(state)
        } else {
            configuredTabs
        }
    }

    private fun startPolling(gameID: String) {
        if (pollingJob?.isActive == true) return
        pollingJob = viewModelScope.launch {
            while (isActive) {
                delay(10000) // Poll every 10 seconds
                val result = getMatchListingUseCase(gameID)
                if (result is Result.Success) {
                    val matches = result.data
                    val currentState = getCurrentUiData()

                    val tabs = if (matches.eventState != currentState?.matches?.eventState) {
                        getTabsForState(matches.eventState)
                    } else {
                        currentState?.tabItems ?: emptyList()
                    }

                    val selectedTab = if (matches.eventState != currentState?.matches?.eventState) {
                        tabs.firstOrNull { it.selected } ?: tabs.firstOrNull()
                    } else {
                        currentState?.selectedTab
                    }

                    val lastInningIndex = matches.innings?.lastOrNull()?.apiInningIndex ?: 1
                    val newInning =
                        if (currentState?.selectedInning == currentState?.matches?.innings?.lastOrNull()?.apiInningIndex) {
                            lastInningIndex
                        } else {
                            currentState?.selectedInning ?: lastInningIndex
                        }

                    val finalMatches = if (newInning != lastInningIndex) {
                        matches.copy(customMetaData = currentState?.matches?.customMetaData)
                    } else {
                        matches
                    }

                    val newState = (currentState ?: MatchCenterUiState()).copy(
                        matches = finalMatches,
                        tabItems = tabs,
                        selectedTab = selectedTab,
                        selectedInning = newInning,
                        totalPlayedInnings = finalMatches.playedInnings
                    )
                    updateSuccessData(newState)
                    refreshDataForSelectedTab(newState, showLoading = false)

                    if (matches.eventState != EventState.LIVE) {
                        stopPolling()
                        break
                    }
                }
            }
        }
    }

    private fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
    }

    fun updateTabItems(items: List<MatchTabItem>) {
        val currentState = getCurrentUiData() ?: MatchCenterUiState()
        updateSuccessData(currentState.copy(tabItems = items))
    }

    override fun onCleared() {
        stopPolling()
        super.onCleared()
    }
}
