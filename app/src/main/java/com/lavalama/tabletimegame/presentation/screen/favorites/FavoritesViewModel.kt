package com.lavalama.tabletimegame.presentation.screen.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lavalama.tabletimegame.domain.entity.TTGame
import com.lavalama.tabletimegame.domain.usecase.AddCommentToGameUseCase
import com.lavalama.tabletimegame.domain.usecase.DeleteCommentToGameUseCase
import com.lavalama.tabletimegame.domain.usecase.GetAllGamesUseCase
import com.lavalama.tabletimegame.domain.usecase.ToggleFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getAllGamesUseCase: GetAllGamesUseCase,
    private val addCommentToGameUseCase: AddCommentToGameUseCase,
    private val deleteCommentToGameUseCase: DeleteCommentToGameUseCase,
    private val toggleFavoritesUseCase: ToggleFavoritesUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<FavoritesScreenState>(FavoritesScreenState.Loading)
    val state: StateFlow<FavoritesScreenState>
        get() = _state.asStateFlow()

    init {
        observeGames()
    }

    fun observeGames() {
        getAllGamesUseCase()
            .onEach { games ->
                _state.update { currentState ->
                    if (currentState is FavoritesScreenState.Content) {
                        currentState.copy(
                            games = games
                        )
                    } else {
                        FavoritesScreenState.Content(games)
                    }
                }
            }
            .catch { throwable ->
                _state.value = FavoritesScreenState.Error(
                    throwable.localizedMessage ?: "Ошибка при загрузке"
                )
            }
            .launchIn(viewModelScope)
    }

    fun processCommand(command: FavoritesScreenCommand) {

        when (command) {
            is FavoritesScreenCommand.DeleteComment -> {
                viewModelScope.launch {
                    deleteCommentToGameUseCase(
                        gameId = command.gameId
                    )
                }
            }

            is FavoritesScreenCommand.ToggleFavorites -> {
                viewModelScope.launch {
                    toggleFavoritesUseCase(
                        gameId = command.gameId
                    )
                }
            }

            is FavoritesScreenCommand.StartEditedComment -> {
                _state.update { currentState ->
                    if (currentState is FavoritesScreenState.Content) {
                        val currentGame = currentState.favoriteGames.firstOrNull { it.id == command.gameId }
                        currentState.copy(
                            editedCommentToGameById = command.gameId,
                            comment = currentGame?.myComment.orEmpty()
                        )
                    } else {
                        currentState
                    }
                }
            }

            is FavoritesScreenCommand.InputComment -> {
                _state.update { currentState ->
                    if (currentState is FavoritesScreenState.Content) {
                        currentState.copy(
                            comment = command.content
                        )
                    } else {
                        currentState
                    }
                }
            }

            FavoritesScreenCommand.SaveComment -> {
                val currentState = _state.value as? FavoritesScreenState.Content ?: return
                viewModelScope.launch {
                    addCommentToGameUseCase(
                        gameId = currentState.editedCommentToGameById,
                        comment = currentState.comment.trim()
                    )
                    _state.update { currentState ->
                        if (currentState is FavoritesScreenState.Content) {
                            currentState.copy(
                                editedCommentToGameById = 0,
                                comment = ""
                            )
                        } else {
                            currentState
                        }
                    }
                }
            }

            FavoritesScreenCommand.CancelEditedComment -> {
                _state.update { currentState ->
                    if (currentState is FavoritesScreenState.Content) {
                        currentState.copy(
                            editedCommentToGameById = 0,
                            comment = ""
                        )
                    } else {
                        currentState
                    }
                }
            }
        }
    }
}

sealed interface FavoritesScreenCommand {
    data class ToggleFavorites(val gameId: Int) : FavoritesScreenCommand
    data class DeleteComment(val gameId: Int) : FavoritesScreenCommand
    data class StartEditedComment(val gameId: Int) : FavoritesScreenCommand
    data class InputComment(val content: String) : FavoritesScreenCommand
    data object SaveComment : FavoritesScreenCommand
    data object CancelEditedComment : FavoritesScreenCommand

}

sealed interface FavoritesScreenState {
    data object Loading : FavoritesScreenState
    data class Error(val message: String) : FavoritesScreenState
    data class Content(
        val games: List<TTGame>,
        val editedCommentToGameById: Int = 0,
        val comment: String = ""
    ) : FavoritesScreenState {
        val isEnableToSaveComment: Boolean
            get() = editedCommentToGameById != 0 && comment.isNotBlank()
        val favoriteGames: List<TTGame>
            get() = games.filter { it.isFavorite }
    }
}