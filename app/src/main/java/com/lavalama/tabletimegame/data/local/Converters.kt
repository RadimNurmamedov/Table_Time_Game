package com.lavalama.tabletimegame.data.local

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class Converters {

    @TypeConverter
    fun fromFiltersMap(filters: Map<String, List<String>>?): String {
        return Json.encodeToString(filters ?: emptyMap())
    }

    @TypeConverter
    fun toFiltersMap(json: String): Map<String, List<String>> {
        if (json.isBlank()) return emptyMap()
        return try {
            Json.decodeFromString(json)
        } catch (e: Exception) {
            emptyMap()
        }
    }

    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun toStringList(json: String): List<String> {
        if (json.isBlank()) return emptyList()
        return try {
            Json.decodeFromString(json)
        } catch (e: Exception) {
            emptyList()

        }
    }
}