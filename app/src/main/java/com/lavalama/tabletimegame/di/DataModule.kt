package com.lavalama.tabletimegame.di

import android.content.Context
import androidx.room.Room
import com.lavalama.tabletimegame.data.local.TTGamesDao
import com.lavalama.tabletimegame.data.local.TTGamesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    companion object{
        @Provides
        @Singleton
        fun provideTTGamesDatabase(
            @ApplicationContext context: Context
        ): TTGamesDatabase{
            return Room.databaseBuilder(
                context = context,
                klass  = TTGamesDatabase::class.java,
                name = "tt_games.db"
            )
                .fallbackToDestructiveMigration(false)
                .build()
        }

        @Provides
        @Singleton
        fun provideTTGamesDao(
            database: TTGamesDatabase
        ): TTGamesDao{
            return database.ttGamesDao()
        }
    }
}