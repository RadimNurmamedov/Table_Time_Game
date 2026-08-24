package com.lavalama.tabletimegame.domain.usecase

import com.lavalama.tabletimegame.domain.repository.TTRepository
import javax.inject.Inject

class DeleteCommentToGameUseCase @Inject constructor(
    private val repository: TTRepository
) {
    suspend operator fun invoke(gameId: Int){
        return repository.deleteCommentToGame(gameId)
    }
}