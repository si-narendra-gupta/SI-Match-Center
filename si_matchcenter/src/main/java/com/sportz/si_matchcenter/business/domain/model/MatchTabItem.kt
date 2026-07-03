package com.sportz.si_matchcenter.business.domain.model

data class MatchTabItem(
    val id: String?,
    val interaction: Boolean?,
    val order: Int?,
    val title: String?,
    val visible: Boolean?,
    val selected: Boolean,
    val sub_tabs: List<MatchTabItem>? = emptyList()
) {
    val tabId: MatchTabId? get() = MatchTabId.fromValue(id)

    companion object {
        fun getDefaultTabsForState(state: EventState): List<MatchTabItem> {
            return when (state) {
                EventState.UPCOMING -> listOf(
                    MatchTabItem(id = "commentary", interaction = true, order = 1, title = "Commentary", visible = true, selected = false),
                    MatchTabItem(id = "teams", interaction = true, order = 2, title = "Teams", visible = true, selected = false),
                    MatchTabItem(id = "match_info", interaction = true, order = 3, title = "Match Info", visible = true, selected = true)
                )

                EventState.LIVE, EventState.RESULT -> listOf(
                    MatchTabItem(id = "commentary", interaction = true, order = 1, title = "Commentary", visible = true, selected = false),
                    MatchTabItem(id = "scorecard", interaction = true, order = 2, title = "Scorecard", visible = true, selected = true),
                    MatchTabItem(id = "teams", interaction = true, order = 3, title = "Teams", visible = true, selected = false),
                    MatchTabItem(
                        id = "graphs",
                        interaction = true,
                        order = 4,
                        title = "Graphs",
                        visible = true,
                        selected = false,
                        sub_tabs = listOf(
                            MatchTabItem(id = "manhattan", interaction = true, order = 1, title = "Manhattan", visible = true, selected = true),
                            MatchTabItem(id = "innings_progression", interaction = true, order = 2, title = "Innings Progression", visible = true, selected = false),
                            MatchTabItem(id = "spider", interaction = true, order = 3, title = "Spider", visible = true, selected = false)
                        )
                    ),
                    MatchTabItem(id = "match_info", interaction = true, order = 5, title = "Match Info", visible = true, selected = false)
                )
            }
        }
    }
}
