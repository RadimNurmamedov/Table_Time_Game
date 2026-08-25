package com.lavalama.tabletimegame.domain.usecase

import com.lavalama.tabletimegame.domain.entity.TTGame
import com.lavalama.tabletimegame.domain.repository.TTRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ToggleFavoritesUseCase @Inject constructor(
    private val repository: TTRepository
) {
    suspend operator fun invoke(gameId: Int) {
        repository.toggleFavorites(gameId)
    }
}