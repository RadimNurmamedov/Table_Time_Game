package com.lavalama.tabletimegame.domain.repository

import com.lavalama.tabletimegame.data.local.TTGameDbModel
import com.lavalama.tabletimegame.domain.entity.TTGame
import kotlinx.coroutines.flow.Flow

interface TTRepository {
    fun getAllGames(): Flow<List<TTGame>>
    suspend fun refreshTTGames(): CodUpdating
    fun getGameById(gameId: Int): Flow<TTGame>
    suspend fun toggleFavorites(gameId: Int)
    suspend fun addCommentToGame(gameId: Int, comment: String)
    suspend fun deleteCommentToGame(gameId: Int)
}
sealed interface CodUpdating{
    data object UpdateProcess: CodUpdating
    data class UpdateErrors(val error: String): CodUpdating
    data object UpdateSuccess: CodUpdating
}