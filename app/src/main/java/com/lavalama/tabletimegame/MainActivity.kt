package com.lavalama.tabletimegame

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.lavalama.tabletimegame.data.repository.TTRepositoryImpl
import com.lavalama.tabletimegame.domain.repository.TTRepository
import com.lavalama.tabletimegame.presentation.screen.gamesCatalog.GamesCatalogScreen
import com.lavalama.tabletimegame.presentation.theme.ui.TableTimeGameTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var repository: TTRepositoryImpl
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TableTimeGameTheme {
                GamesCatalogScreen(
                    onSettingsNavigate = {},
                    onFavoriteNavigate = {},
                )
            }
        }
    }
}