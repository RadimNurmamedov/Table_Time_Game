package com.lavalama.tabletimegame.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TTGamesResponseDto(
    @SerialName("games")
    val ttGames: List<TTGameDto> = listOf()
)