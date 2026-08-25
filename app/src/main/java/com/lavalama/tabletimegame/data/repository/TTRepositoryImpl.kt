package com.lavalama.tabletimegame.data.repository

import android.util.Log
import com.lavalama.tabletimegame.data.local.TTGameDbModel
import com.lavalama.tabletimegame.data.local.TTGamesDao
import com.lavalama.tabletimegame.data.mapper.toDbModel
import com.lavalama.tabletimegame.data.mapper.toEntities
import com.lavalama.tabletimegame.data.mapper.toEntity
import com.lavalama.tabletimegame.data.remote.TTGamesApiService
import com.lavalama.tabletimegame.domain.entity.TTGame
import com.lavalama.tabletimegame.domain.repository.TTRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class TTRepositoryImpl @Inject constructor(
    private val dao: TTGamesDao,
    private val apiService: TTGamesApiService
) : TTRepository {
    suspend fun loadTTGames(): List<TTGameDbModel> {
        return try {
            apiService.loadGames().toDbModel()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.e("TTGamesLogs", "NETWORK ERROR: ${e.message}", e)
            listOf()
        }
    }

    override fun getAllGames(): Flow<List<TTGame>> {
        return dao.getTTGames().map {
            it.toEntities()
        }
    }

    override suspend fun getGameById(gameId: Int): TTGame {
        return dao.getTTGameById(gameId).toEntity()
    }

    override suspend fun toggleFavorites(gameId: Int) {
        dao.toggleFavorites(gameId)
    }

    override suspend fun addCommentToGame(gameId: Int, comment: String) {
        dao.addCommentToGame(gameId, comment)
    }

    override suspend fun deleteCommentToGame(gameId: Int) {
        dao.deleteCommentToGame(gameId)
    }
}