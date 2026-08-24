package com.lavalama.tabletimegame.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TTGamesDao {
    @Query("SELECT * FROM tt_games")
    fun getTTGames(): Flow<List<TTGameDbModel>>
    @Query("SELECT * FROM tt_games WHERE id = :gameId")
    fun getTTGameBuId(gameId: Int): TTGameDbModel
    @Query("UPDATE tt_games SET isFavorite = NOT isFavorite WHERE id == :gameId")
    suspend fun toggleFavorites(gameId: Int)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCommentToGame(comment: String)
    @Query("DELETE FROM tt_games WHERE id == :gameId")
    suspend fun deleteCommentToGame(gameId: Int)
}