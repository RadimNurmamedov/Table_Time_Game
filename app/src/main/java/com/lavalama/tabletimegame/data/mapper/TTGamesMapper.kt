package com.lavalama.tabletimegame.data.mapper

import com.lavalama.tabletimegame.data.local.TTGameDbModel
import com.lavalama.tabletimegame.data.remote.TTGameDto
import com.lavalama.tabletimegame.data.remote.TTGamesResponseDto
import com.lavalama.tabletimegame.domain.entity.TTGame
import com.lavalama.tabletimegame.domain.entity.TTImproMember


fun List<TTGameDto>.toDbModel(): List<TTGameDbModel> {
    return this.map {
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

fun List<TTGameDbModel>.toEntities(): List<TTGame> {
    return map { gameDbModel ->
        TTGame(
            id = gameDbModel.id,
            title = gameDbModel.title,
            season = gameDbModel.season,
            episodeNumber = gameDbModel.episodeNumber,
            host = TTImproMember.valueOf(gameDbModel.host),
            shortRules = gameDbModel.shortRules,
            fullRules = gameDbModel.fullRules,
            genre = gameDbModel.genre,
            winner = TTImproMember.valueOf(gameDbModel.winner),
            imageUrl = gameDbModel.imageUrl,
            isFavorite = gameDbModel.isFavorite,
            myComment = gameDbModel.myComment
        )
    }
}

fun TTGameDbModel.toEntity(): TTGame {
    return TTGame(
        id = id,
        title = title,
        season = season,
        episodeNumber = episodeNumber,
        host = TTImproMember.valueOf(host),
        shortRules = shortRules,
        fullRules = fullRules,
        genre = genre,
        winner = TTImproMember.valueOf(winner),
        imageUrl = imageUrl,
        isFavorite = isFavorite,
        myComment = myComment
    )
}

