package com.lavalama.tabletimegame.data.remote

import retrofit2.http.GET

interface TTGamesApiService {
    @GET("data/games.json")//эндпоинт запроса
    suspend fun loadGames(): List<TTGameDto>
}