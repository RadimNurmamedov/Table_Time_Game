package com.lavalama.tabletimegame.presentation.screen.gamesCatalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lavalama.tabletimegame.domain.entity.TTGame
import com.lavalama.tabletimegame.domain.usecase.GetAllGamesUseCase
import com.lavalama.tabletimegame.domain.usecase.StartRefreshDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamesCatalogViewModel @Inject constructor(
    private val startRefreshDataUseCase: StartRefreshDataUseCase,
    private val getAllGamesUseCase: GetAllGamesUseCase,

    ) : ViewModel() {
    private val _state = MutableStateFlow(GamesCatalogState())
    val state: StateFlow<GamesCatalogState>
        get() = _state.asStateFlow()


    init {
        viewModelScope
            .launch {
                startRefreshDataUseCase()
            }
        observeTTGamesFlow()
    }

    private fun observeTTGamesFlow() {
        getAllGamesUseCase()
            .onEach { games ->
                _state.update { currentState ->
                    currentState.copy(ttGames = games)
                }
            }.launchIn(viewModelScope)
    }

    fun processCommand(command: GamesCatalogCommand) {
        when (command) {
            is GamesCatalogCommand.InputGameTitle -> {
                _state.update { currentState ->
                    currentState.copy(searchQuery = command.query)
                }
            }
        }
    }
}

sealed interface GamesCatalogCommand {
    data class InputGameTitle(val query: String) : GamesCatalogCommand

}

data class GamesCatalogState(
    val ttGames: List<TTGame> = listOf(),
    val searchQuery: String = ""
) {
    val filteredGames: List<TTGame>
        get() = if (searchQuery.isBlank()) {
            ttGames
        } else {
            ttGames.filter { game ->
                game.title.contains(searchQuery, ignoreCase = true)
            }
        }
}