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
            winner = it.winner,
            imageUrl = it.imageUrl,
            isFavorite = false,
            myComment = "",
            filters = it.filters,
            quotes = it.quotes,
            inventory = it.inventory,
            videoUrl = it.videoUrl,
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
            winner = TTImproMember.valueOf(gameDbModel.winner),
            imageUrl = gameDbModel.imageUrl,
            isFavorite = gameDbModel.isFavorite,
            myComment = gameDbModel.myComment,
            filters = gameDbModel.filters,
            quotes = gameDbModel.quotes,
            inventory = gameDbModel.inventory,
            videoUrl = gameDbModel.videoUrl,
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
        winner = TTImproMember.valueOf(winner),
        imageUrl = imageUrl,
        isFavorite = isFavorite,
        myComment = myComment,
        filters = filters,
        quotes = quotes,
        inventory = inventory,
        videoUrl = videoUrl,
    )
}

