package com.lavalama.tabletimegame.presentation.screen.gameDetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.lavalama.tabletimegame.R
import com.lavalama.tabletimegame.domain.entity.TTGame

@Composable
fun GameDetailsScreen(
    modifier: Modifier = Modifier,
    gameId: Int,
    viewModel: GameDetailsViewModel = hiltViewModel(
        creationCallback = { factory: GameDetailsViewModel.Factory ->
            factory.create(gameId)
        }
    ),
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    when (val currentState = state) {
        is GameDetailsState.Content -> {
            GameDetailsStateContent(
                modifier = modifier,
                game = currentState.game,
                onBack = onBack,
                onToggleFavorites = {
                    viewModel.processCommand(GameDetailsCommand.ToggleFavorites)
                }
            )
        }

        is GameDetailsState.Error -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        GameDetailsState.Loading -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailsStateContent(
    modifier: Modifier = Modifier,
    game: TTGame,
    onBack: () -> Unit,
    onToggleFavorites: () -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(game.title)
                },
                navigationIcon = {
                    IconButton(onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onToggleFavorites) {
                        if (game.isFavorite) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Remove from favorites",
                                tint = Color.Red
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = "Add to favorites"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding,
        ) {
            item {
                CardPoster(imageUrl = game.imageUrl)
            }
            item {
                CardDetail(
                    title = "Описание",
                    content = game.shortRules
                )
            }
            item {
                CardDetail(
                    title = "Правила",
                    content = game.fullRules
                )
            }
            item {
                CardDetail(
                    title = "Ведущий",
                    content = game.host.displayName
                )
            }
            item {
                CardDetail(
                    title = "Победитель",
                    content = game.winner.displayName
                )
            }
            item {
                CardDetail(
                    title = "Жанры",
                    content = game.genre.joinToString(";")
                )
            }
        }
    }
}

@Composable
fun CardPoster(
    modifier: Modifier = Modifier,
    imageUrl: String,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
//         Картинка во всю ширину с сохранением пропорций 16:9
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.ic_launcher_background),
            placeholder = painterResource(R.drawable.ic_launcher_background)
        )
    }
}

@Composable
fun CardDetail(
    modifier: Modifier = Modifier,
    title: String,
    content: String
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            ,
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(4.dp))
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = content,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}