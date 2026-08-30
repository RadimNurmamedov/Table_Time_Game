package com.lavalama.tabletimegame.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tt_games",
    indices = [Index("host")]
)
data class TTGameDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val season: Int,
    val episodeNumber: Int,
    val host: String, // Кто принес игру
    val shortRules: String,  // Краткое описание
    val fullRules: String,   // Подробные правила
    val winner: String, // Кто забрал баллы/раунд
    val imageUrl: String,
    val isFavorite: Boolean = false,
    val myComment: String = "",

    val filters: Map<String, List<String>>,
    val quotes: List<String>,
    val inventory: List<String>,
    val videoUrl: String,
    )
