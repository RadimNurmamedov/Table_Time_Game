package com.lavalama.tabletimegame.presentation.screen.gamesCatalog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.room.util.query
import coil3.compose.AsyncImage
import com.lavalama.tabletimegame.R
import com.lavalama.tabletimegame.domain.entity.TTGame

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesCatalogScreen(
    modifier: Modifier = Modifier,
    onSettingsNavigate: () -> Unit,
    onFavoriteNavigate: () -> Unit,
    onGameDetailsNavigate: (Int) -> Unit,
    viewModel: GamesCatalogViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Table Time") },
                actions = {
                    IconButton(
                        onClick = onSettingsNavigate
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Open Settings screen"
                        )
                    }
                    IconButton(
                        onClick = onFavoriteNavigate
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Open Favorites games screen",
                            tint = Color.Red
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding
        ) {

            item {
                SearchPanel(
                    query = state.searchQuery,
                    onInputQuery = {
                        viewModel.processCommand(GamesCatalogCommand.InputGameTitle(it))
                    }
                )
            }

            val games = state.ttGames
            if (games.isNotEmpty()){
                val seasonCounts = games.last().season
                for (season in 1..seasonCounts){
                    val findsGames = state.filteredGames
                    val filteredGames = findsGames.filter {game ->
                        game.season == season
                    }
                    item {
                        SeasonHeader(season)
                    }
                    item {
                        Spacer(Modifier.height(8.dp))
                    }
                    items(items = filteredGames, key = { it.id }){game ->
                        GameCard(
                            game = game,
                            onClick = {
                                onGameDetailsNavigate(it)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchPanel(
    modifier: Modifier = Modifier,
    query: String = "",
    onInputQuery: (String) -> Unit
){
    Row(
        modifier = modifier
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = query,
            onValueChange = {
                onInputQuery(it)
            },
            readOnly = false,
            label = {
                Text("Enter game title")
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search game"
                )
            },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
        )
        Spacer(Modifier.width(4.dp))
    }
}
@Composable
fun SeasonHeader(seasonNumber: Int) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Text(
            text = "Сезон $seasonNumber",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
}
@Composable
fun GameCard(
    game: TTGame,
    onClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick(game.id) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Постер серии
            AsyncImage(
                model = game.imageUrl,
                contentDescription = game.title,
                modifier = Modifier
                    // Фиксируем высоту, а ширину вычисляем по пропорции
                    .height(64.dp)
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(8.dp)), // Скругляем саму картинку
                contentScale = ContentScale.Crop, // Заполняем прямоугольник без полей
                error = painterResource(R.drawable.ic_launcher_background),
                placeholder = painterResource(R.drawable.ic_launcher_background)
            )
            Spacer(modifier = Modifier.width(16.dp))
            // Описание игры
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = game.title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${game.episodeNumber} серия",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            // Индикатор избранного
            if (game.isFavorite) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}