package com.lavalama.tabletimegame.domain.usecase

import com.lavalama.tabletimegame.domain.entity.TTGame
import com.lavalama.tabletimegame.domain.repository.TTRepository
import javax.inject.Inject

class GetGameByIdUseCase @Inject constructor(
    private val repository: TTRepository
) {
      suspend operator fun invoke(id: Int): TTGame{
        return repository.getGameById(id)
    }
}