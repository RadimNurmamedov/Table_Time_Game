package com.lavalama.tabletimegame.data.local

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromGenreList(genres: List<String>): String {
        return Json.encodeToString(genres)
    }

    @TypeConverter
    fun toGenreList(genresJson: String): List<String> {
        return try {
            Json.decodeFromString(genresJson)
        } catch (e: Exception) {
            emptyList()
        }
    }
}