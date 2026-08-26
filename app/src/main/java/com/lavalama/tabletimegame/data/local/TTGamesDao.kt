package com.lavalama.tabletimegame.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TTGamesDao {
    @Query("SELECT * FROM tt_games")
    fun getTTGames(): Flow<List<TTGameDbModel>>
    @Query("SELECT * FROM tt_games WHERE id = :gameId")
    fun getTTGameById(gameId: Int): Flow<TTGameDbModel>
    @Query("UPDATE tt_games SET isFavorite = NOT isFavorite WHERE id == :gameId")
    suspend fun toggleFavorites(gameId: Int)
    @Query("UPDATE tt_games SET myComment = :comment WHERE id = :gameId")
    suspend fun addCommentToGame(gameId: Int, comment: String)
    @Query("UPDATE tt_games SET myComment = '' WHERE id = :gameId")
    suspend fun deleteCommentToGame(gameId: Int)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTTGames(games: List<TTGameDbModel>): List<Long>
}