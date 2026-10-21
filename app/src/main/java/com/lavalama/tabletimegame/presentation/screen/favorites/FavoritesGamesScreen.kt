package com.lavalama.tabletimegame.presentation.screen.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.lavalama.tabletimegame.R
import com.lavalama.tabletimegame.domain.entity.TTGame
import com.lavalama.tabletimegame.presentation.navigation.Screen

@Composable
fun FavoritesGamesScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel(),
    onGameClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    when (val currentState = state) {
        is FavoritesScreenState.Content -> {
            Content(
                modifier = modifier,
                onGameClick = onGameClick,
                onBack = onBack,
                state = currentState,
                onStartEditComment = { gameId ->
                    viewModel.processCommand(FavoritesScreenCommand.StartEditedComment(gameId))
                },
                onCommentChanged = { content ->
                    viewModel.processCommand(FavoritesScreenCommand.InputComment(content))
                },
                onCancelEditedComment = {
                    viewModel.processCommand(FavoritesScreenCommand.CancelEditedComment)
                },
                onSaveComment = {
                    viewModel.processCommand(FavoritesScreenCommand.SaveComment)
                }
            )
        }

        is FavoritesScreenState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentState.message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        FavoritesScreenState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(
    modifier: Modifier = Modifier,
    onGameClick: (Int) -> Unit,
    onBack: () -> Unit,
    state: FavoritesScreenState.Content,
    onStartEditComment: (Int) -> Unit,
    onCommentChanged: (String) -> Unit,
    onCancelEditedComment: () -> Unit,
    onSaveComment: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.favorites_games))
                },
                navigationIcon = {
                    IconButton(
                        onBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding
        ) {
            if (state.favoriteGames.isEmpty()){
                item {
                    Card(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = stringResource(R.string.not_added_favorites_games),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            } else {
                items(
                    items = state.favoriteGames,
                    key = { it.id }
                ) { game ->
                    val isEditingThisGame = state.editedCommentToGameById == game.id
                    FavoritesGameCard(
                        game = game,
                        isEditing = isEditingThisGame,
                        editingText = if (isEditingThisGame) state.comment else game.myComment,
                        isSaveEnabled = state.isEnableToSaveComment,
                        onGameClick = { onGameClick(game.id) },
                        onStartEditComment = { onStartEditComment(game.id) },
                        onCommentChanged = onCommentChanged,
                        onCancelEditedComment = onCancelEditedComment,
                        onSaveComment = onSaveComment
                    )
                }
            }
        }
    }
}

@Composable
fun FavoritesGameCard(
    modifier: Modifier = Modifier,
    game: TTGame,
    isEditing: Boolean,
    editingText: String,
    isSaveEnabled: Boolean,
    onGameClick: () -> Unit,
    onStartEditComment: () -> Unit,
    onCommentChanged: (String) -> Unit,
    onCancelEditedComment: () -> Unit,
    onSaveComment: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        onClick = onGameClick,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        AsyncImage(
            model = game.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
//                .clip(RoundedCornerShape(16.dp)),
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.ic_launcher_background),
            placeholder = painterResource(R.drawable.ic_launcher_background)
        )
        Spacer(Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = game.title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = game.shortRules,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(4.dp))
            if (isEditing) {
                OutlinedTextField(
                    value = editingText,
                    onValueChange = onCommentChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.my_comment)) },
                    leadingIcon = {
                        IconButton(onClick = onCancelEditedComment) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.cancel)
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = onSaveComment,
                            enabled = isSaveEnabled
                        ) {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = stringResource(R.string.save)
                            )
                        }
                    },
                    maxLines = 3
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onStartEditComment() }
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = game.myComment.ifBlank { stringResource(R.string.add_a_comment) },
                        style = MaterialTheme.typography.bodySmall,
                        color = if (game.myComment.isNotBlank()) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(end = 32.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.edit_comment),
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
        }
    }
}