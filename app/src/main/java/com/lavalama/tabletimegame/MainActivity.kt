package com.lavalama.tabletimegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lavalama.tabletimegame.data.repository.TTRepositoryImpl
import com.lavalama.tabletimegame.presentation.navigation.NavGraph
import com.lavalama.tabletimegame.presentation.screen.gameDetails.GameDetailsScreen
import com.lavalama.tabletimegame.presentation.theme.ui.TableTimeGameTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var repository: TTRepositoryImpl
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TableTimeGameTheme {
                NavGraph()
            }
        }
    }
}