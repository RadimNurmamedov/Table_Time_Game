package com.lavalama.tabletimegame.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [TTGameDbModel::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TTGamesDatabase: RoomDatabase() {
    abstract fun ttGamesDao(): TTGamesDao
}