package com.lavalama.tabletimegame.domain.repository

import com.lavalama.tabletimegame.domain.entity.TTGame
import kotlinx.coroutines.flow.Flow

interface TTRepository {
    fun getAllGames(): Flow<List<TTGame>>
    fun getGameById(gameId: Int): TTGame
    suspend fun toggleFavorites(gameId: Int): Boolean
    suspend fun addCommentToGame(gameId: Int, comment: String)
}