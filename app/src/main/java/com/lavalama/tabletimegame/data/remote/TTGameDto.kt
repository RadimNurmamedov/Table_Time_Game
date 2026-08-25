package com.lavalama.tabletimegame.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TTGameDto(
    @SerialName("gameTitle")
    val title: String = "",
    @SerialName("season")
    val season: Int = 0,
    @SerialName("episodeNumber")
    val episodeNumber: Int = 0,
    @SerialName("host")
    val host: String, // Кто принес игру
    @SerialName("shortRules")
    val shortRules: String,  // Краткое описание

    @SerialName("fullRules")
    val fullRules: String,   // Подробные правила
    @SerialName("genre")
    val genre: List<String>,       // Блеф, Реакция, Ассоциации и т.д.
    @SerialName("winner")
    val winner: String, // Кто забрал баллы/раунд
    @SerialName("imageUrl")
    val imageUrl: String,
)
