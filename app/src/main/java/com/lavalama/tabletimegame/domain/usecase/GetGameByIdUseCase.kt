package com.lavalama.tabletimegame.domain.usecase

import com.lavalama.tabletimegame.domain.entity.TTGame
import com.lavalama.tabletimegame.domain.repository.TTRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGameByIdUseCase @Inject constructor(
    private val repository: TTRepository
) {
      operator fun invoke(id: Int): Flow<TTGame>{
        return repository.getGameById(id)
    }
}