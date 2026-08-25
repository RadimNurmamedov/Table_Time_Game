package com.lavalama.tabletimegame.domain.usecase

import com.lavalama.tabletimegame.domain.repository.TTRepository
import javax.inject.Inject

class StartRefreshDataUseCase @Inject constructor(
    private val ttGameRepository: TTRepository
) {
    suspend operator fun invoke() {
        ttGameRepository.refreshTTGames()
    }
}