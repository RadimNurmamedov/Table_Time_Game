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
                    currentState.copy(
                        ttGames = games,
                        availableFilters = getAvailableFilters(games),
                    )
                }
            }.launchIn(viewModelScope)
    }
    private fun getAvailableFilters(allGames: List<TTGame>): Map<String, List<String>>{
        if (allGames.isEmpty()) return emptyMap()
        // 1. Собираем все уникальные категории (ключи) со всех игр
        val allKeys = allGames
            .flatMapTo(HashSet()) {
                it.filters.keys
            }
        // 2. Для каждого ключа собираем уникальные теги со всех игр
        return allKeys.associateWith { key ->
            allGames
                .flatMap { game ->
                    game.filters[key].orEmpty()
                }
                .distinct()
        }
    }

    fun processCommand(command: GamesCatalogCommand) {
        when (command) {
            is GamesCatalogCommand.InputGameTitle -> {
                _state.update { currentState ->
                    currentState.copy(
                        searchQuery = command.query
                    )
                }
            }
            is GamesCatalogCommand.ChangeFilters -> {
                _state.update { currentState ->
                    currentState.copy(
                        currentFilters = currentState.currentFilters + (command.filter to command.newValues)
                    )
                }
            }
            GamesCatalogCommand.ClearAllFilters -> {
                _state.update { currentState ->
                    currentState.copy(currentFilters = emptyMap())
                }
            }

            GamesCatalogCommand.ClearQuery -> {
                _state.update { currentState ->
                    currentState.copy(searchQuery = "")
                }
            }
        }
    }
}

sealed interface GamesCatalogCommand {
    data class InputGameTitle(val query: String) : GamesCatalogCommand

    data class ChangeFilters(
        val filter: String,
        val newValues: List<String>
    ) : GamesCatalogCommand

    data object ClearAllFilters : GamesCatalogCommand
    data object ClearQuery : GamesCatalogCommand
}

data class GamesCatalogState(
    val ttGames: List<TTGame> = listOf(),
    val searchQuery: String = "",
    val availableFilters: Map<String, List<String>> = emptyMap(),
    val currentFilters: Map<String, List<String>> = emptyMap()
) {
    val sortedFilterKeys: List<String>
        get() = availableFilters.keys.sortedByDescending {key->
            currentFilters[key]?.isNotEmpty() == true
        }
    val filteredGames: List<TTGame>
    get() {
        // Отфильтровываем пустые списки выбранных фильтров
        val activeFilters = currentFilters.filterValues {
            it.isNotEmpty()
        }
        return ttGames.filter { game ->
            // 1. Проверка по поисковому запросу
            val matchesQuery = searchQuery.isBlank() ||
                    game.title.contains(searchQuery, ignoreCase = true)

            if (!matchesQuery) return@filter false

            // 2. Проверка по фильтрам:
            // .all = AND между РАЗНЫМИ категориями (напр. Инвентарь И Время)
            // .any = OR внутри ОДНОЙ категории (напр. "Есть" ИЛИ "Нет")
            activeFilters.all { (category, selectedValues) ->
                val gameCategoryValues = game.filters[category].orEmpty()

                selectedValues.any { selectedTag ->
                    gameCategoryValues.contains(selectedTag)
                }
            }
        }
    }
}