package com.sportz.si_matchcenter.business.domain.model.themecolor

data class MatchCenterThemeColors(
    val header: Header? = null,
    val last_balls: LastBalls? = null,
    val live_badge: Badge? = null,
    val result_badge: Badge? = null,
    val upcoming_badge: Badge? = null,
    val match_card: MatchCard? = null,
    val performer_section: PerformerSection? = null,
    val player_of_match: PlayerOfMatch? = null,
    val screen: Screen? = null,
    val team: Team? = null,
    val team_logos: TeamLogos? = null,
    val match_details: CustomTabDTO? = null,
) {
    companion object {

        fun Companion.default() = MatchCenterThemeColors(
            screen = Screen(background = "#FFFFFF"),
            match_card = MatchCard(
                gradient_start = "#07167A", gradient_end = "#950461", border = "#FFFFFF33"
            ),
            header = Header(
                title = "#95045A",
                subtitle = "#FFFFFF",
                match_info_text = "#FFFFFF",
                divider = "#33FFFFFF",
                header_background = "#07167A"
            ),
            live_badge = Badge(background = "#008632", text = "#FFFFFF"),
            upcoming_badge = Badge(background = "#E0E0E0", text = "#1C1C28"),
            result_badge = Badge(background = "#DE3333", text = "#FFFFFF"),
            team = Team(
                yet_to_bat = "#1C1C2880",
                highlight_color = "#FFFFFF",
                name = "#80FFFFFF",
                score = "#80FFFFFF",
                overs = "#80FFFFFF",
                vs_background = "#1AFFFFFF",
                vs_text = "#FFFFFF",
                venue = "#FFFFFF",
                equation = "#FFFFFF",
                time_left = "#80FFFFFF",
                time_left_label = "#80FFFFFF",
                time_left_divider = "#80FFFFFF"
            ),
            player_of_match = PlayerOfMatch(
                background = "#4D181825", label = "#FFFFFF", player_name = "#E81FA0"
            ),
            performer_section = PerformerSection(
                background = "#0DFFFFFF",
                header_background = "#1AFFFFFF",
                header_text = "#FFFFFF",
                icon = "#FFFFFF",
                player_name = "#FFFFFF",
                stats = "#FFFFFF"
            ),
            team_logos = TeamLogos(background = "#FFFFFF", border = "#E50695"),
            last_balls = LastBalls(
                background = "#181825",
                label = "#FFFFFF",
                dot_ball_background = "#FFFFFF",
                dot_ball_text = "#1C1C28",
                four_background = "#1226AA",
                four_text = "#FFFFFF",
                six_background = "#E96BB0",
                six_text = "#FFFFFF",
                wicket_background = "#DE3333",
                wicket_text = "#FFFFFF"
            ),
            match_details = CustomTabDTO(
                graph = null, match_info = MatchInfo(
                    label = "#1C1C28",
                    value = "#950461",
                    background = "#FFFFFF",
                    horizontal_divider = "#1A000000"
                ), teams = PlayingTeam(
                    border = "#B3FFFFFF",
                    background = "#FFFFFF",
                    left_divider = "#1226AA",
                    vertical_divider = "#1A000000",
                    first_name = "#B31C1C28",
                    last_name = "#1C1C28",
                    skill = "#B31C1C28",
                    header_title = "#1C1C28"
                ), scorecard = Scorecard(
                    impact_player = ImpactPlayer(
                        background = "#1226AA",
                        title = "#FFFFFF",
                        header = "#FFFFFF",
                        player_name = "#FFFFFF"
                    ), inning_team = InningTeam(
                        background = "#E50695",
                        team_background = "#F5F5F5",
                        team_name = "#1C1C28",
                        role = "#1C1C28",
                        score = "#1C1C28",
                        run_rate = "#1C1C28",
                        stats_label = "#1C1C28",
                        stats_background = "#1226AA",
                        player_name = "#1C1C28",
                        player_equation = "#B31C1C28",
                        stats_value = "#1C1C28",
                        horizontal_divider = "#1A1C1C28"
                    ), extra_total = ExtraTotal(
                        background = "#940E60",
                        extra_label = "#FFFFFF",
                        extra_value = "#FFFFFF",
                        total_label = "#DE3333",
                        total_value = "#DE3333"
                    ), fall_of_wicket = FallOfWicket(
                        heading = "#1C1C28",
                        background = "#1226AA",
                        score = "#B31C1C28",
                        player_name = "#1C1C28",
                        over = "#1C1C28",
                        horizontal_divider = "#1A1C1C28"
                    )
                ), commentary_card = CommentaryCard(
                    background = "#FFFFFF",
                    commentary_header = CommentaryHeader(
                        background = "#1226AA", over = "#FFFFFF", score = "#FFFFFF"
                    ),
                    title = "#333333",
                    description = "#616161",
                    over = "#D81B60",
                    divider = "#D81B60",
                    ball = Ball(
                        dot_bg = "#E0E0E0",
                        dot_text = "#424242",
                        four_bg = "#2962FF",
                        four_text = "#FFFFFF",
                        six_bg = "#EC407A",
                        six_text = "#FFFFFF",
                        wicket_bg = "#E53935",
                        wicket_text = "#FFFFFF"
                    )
                ), tab = Tab(
                    border = "#E0E0E0",
                    background = "#FFFFFF",
                    selected_background = "#D81B60",
                    selected_text = "#FFFFFF",
                    unselected_text = "#757575"
                ), sub_tab = SubTab(
                    divider = "#E0E0E0",
                    selected_divider = "#D81B60",
                    selected_text = "#D81B60",
                    unselected_text = "#757575"
                ), screen = Screen(background = "#F5F5F5")
            )
        )
    }
}
