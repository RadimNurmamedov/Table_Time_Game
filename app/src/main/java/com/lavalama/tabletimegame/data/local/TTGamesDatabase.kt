package com.lavalama.tabletimegame.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TTGameDbModel::class],
    version = 1,
    exportSchema = false
)
abstract class TTGamesDatabase: RoomDatabase() {
    abstract fun ttGamesDao(): TTGamesDao
}