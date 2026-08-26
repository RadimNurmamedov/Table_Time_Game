package com.lavalama.tabletimegame.presentation.screen.gameDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lavalama.tabletimegame.domain.entity.TTGame
import com.lavalama.tabletimegame.domain.usecase.AddCommentToGameUseCase
import com.lavalama.tabletimegame.domain.usecase.DeleteCommentToGameUseCase
import com.lavalama.tabletimegame.domain.usecase.GetGameByIdUseCase
import com.lavalama.tabletimegame.domain.usecase.ToggleFavoritesUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
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

@HiltViewModel(assistedFactory = GameDetailsViewModel.Factory::class)
class GameDetailsViewModel @AssistedInject constructor(
    val getGameByIdUseCase: GetGameByIdUseCase,
    val toggleFavoritesUseCase: ToggleFavoritesUseCase,
    @Assisted("gameId") private val gameId: Int
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("gameId") gameId: Int
        ): GameDetailsViewModel
    }

    private val _state = MutableStateFlow<GameDetailsState>(GameDetailsState.Loading)
    val state: StateFlow<GameDetailsState>
        get() = _state.asStateFlow()

    init {
        observeGameDetails()
    }
    private fun observeGameDetails() {
        getGameByIdUseCase(gameId)
            .onEach { game ->
                _state.update { currentState ->
                    GameDetailsState.Content(game)
                }
            }
            .catch { throwable ->
                _state.value = GameDetailsState.Error(
                    message = throwable.localizedMessage ?: "Произошла ошибка при загрузке"
                )
            }
            .launchIn(viewModelScope)
    }

    fun processCommand(command: GameDetailsCommand) {
        when (command) {
            GameDetailsCommand.ToggleFavorites -> {
                viewModelScope.launch {
                    toggleFavoritesUseCase(gameId)
                }
            }
        }
    }
}

sealed interface GameDetailsCommand {
    data object ToggleFavorites : GameDetailsCommand
}

sealed interface GameDetailsState {
    data object Loading : GameDetailsState
    data class Content(val game: TTGame) : GameDetailsState
    data class Error(val message: String) : GameDetailsState
}

