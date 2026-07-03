package com.sportz.simatchcenter.presentation.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.sportz.si_matchcenter.helper.ThemeConstants
import com.sportz.si_matchcenter.presentation.ui.screen.MatchCenterContent
import com.sportz.simatchcenter.presentation.ui.screen.matchcenter.MatchCenterScreen
import com.sportz.simatchcenter.presentation.ui.theme.SIMatchCenterTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            SIMatchCenterTheme {
                /*MatchCenterScreen(
                    gameId = "rrck03302026267884",
                    theme = ThemeConstants.DEFAULT_THEME
                )*/
                MatchCenterContent(
                    gameId = "rrck03302026267884",
                    theme = ThemeConstants.DEFAULT_THEME
                )
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_7)
@Preview(
    showBackground = true,
    device = Devices.PIXEL_7,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode"
)
@Composable
fun GreetingPreview() {
    SIMatchCenterTheme {
        MatchCenterScreen(
            gameId = "enwsaw07022026262349", theme = ThemeConstants.THEME_PINK_NAVY
        )
    }
}
