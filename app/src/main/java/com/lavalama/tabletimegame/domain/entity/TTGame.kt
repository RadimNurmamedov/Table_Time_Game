package com.lavalama.tabletimegame.domain.entity

data class TTGame(
    val id: Int,
    val title: String,
    val season: Int,
    val episodeNumber: Int,
    val shortRules: String,  // Краткое описание
    val fullRules: String,   // Подробные правила
    val filters: Map<String, List<String>>,
    val quotes: List<String>,
    val inventory: List<String>,
    val host: TTImproMember, // Кто принес игру
    val winner: TTImproMember, // Кто забрал баллы/раунд
    val imageUrl: String,
    val videoUrl: String,
    val isFavorite: Boolean = false,
    val myComment: String = ""
)
