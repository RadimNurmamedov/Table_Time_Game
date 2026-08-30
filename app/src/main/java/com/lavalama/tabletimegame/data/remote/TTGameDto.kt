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
    @SerialName("winner")
    val winner: String, // Кто забрал баллы/раунд
    @SerialName("imageUrl")
    val imageUrl: String,

    @SerialName("filters")
    val filters: Map<String, List<String>>,
    @SerialName("quotes")
    val quotes: List<String>,
    @SerialName("inventory")
    val inventory: List<String>,
    @SerialName("videoUrl")
    val videoUrl: String,

    )
