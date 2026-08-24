package com.lavalama.tabletimegame.domain.entity

data class TTGame(
    val title: String,
    val season: Int,
    val episodeNumber: Int,
    val host: TTImproMember, // Кто принес игру
    val shortRules: String,  // Краткое описание
    val fullRules: String,   // Подробные правила
    val genre: List<String>,       // Блеф, Реакция, Ассоциации и т.д.
    val winner: TTImproMember, // Кто забрал баллы/раунд
    val imageUrl: String,
    val isFavorite: Boolean = false,
    val myComment: String = ""
)
