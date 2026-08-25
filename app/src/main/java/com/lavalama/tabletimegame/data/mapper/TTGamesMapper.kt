package com.lavalama.tabletimegame.data.mapper

import com.lavalama.tabletimegame.data.local.TTGameDbModel
import com.lavalama.tabletimegame.data.remote.TTGamesResponseDto

class TTGamesMapper {
    fun TTGamesResponseDto.toDbModel(): List<TTGameDbModel>{
        return ttGames.map {
            TTGameDbModel(
                id = 0,
                title = it.title,
                season = it.season,
                episodeNumber = it.episodeNumber,
                host = it.host,
                shortRules = it.shortRules,
                fullRules = it.fullRules,
                genre = it.genre,
                winner = it.winner,
                imageUrl = it.imageUrl,
                isFavorite = false,
                myComment = ""
            )
        }
    }

}