package com.sportz.simatchcenter.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import com.sportz.si_matchcenter.MatchCenterSDK
import com.sportz.si_matchcenter.helper.ThemeConstants
import com.sportz.si_matchcenter.presentation.ui.viewmodel.MatchCenterViewModel
import com.sportz.simatchcenter.presentation.ui.screen.matchcenter.MatchCenterScreen
import com.sportz.simatchcenter.presentation.ui.theme.SIMatchCenterTheme

class MainActivity : ComponentActivity() {

    private val matchCenterViewModel: MatchCenterViewModel by viewModels {
        MatchCenterSDK.provideViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        //enableEdgeToEdge()
        setContent {
            SIMatchCenterTheme {
                MatchCenterScreen(
                    gameId = "enwauw07052026262350", 
                    theme = ThemeConstants.THEME_DARK_EMERALD,
                    viewModel = matchCenterViewModel,
                )
            }
        }
    }
}

@Composable
fun GreetingPreview() {
    SIMatchCenterTheme {
        // Preview might need a mock or just not be shown if VM factory requires initialization
        // For simplicity, passing a placeholder here if needed, or keeping it as is if it's just for preview.
        // In a real app, you'd use a mock ViewModel.
    }
}
