package com.lavalama.tabletimegame.di

import android.content.Context
import androidx.room.Room
import com.lavalama.tabletimegame.data.local.TTGamesDao
import com.lavalama.tabletimegame.data.local.TTGamesDatabase
import com.lavalama.tabletimegame.data.remote.TTGamesApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton
import kotlin.jvm.java


@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    companion object{
        @Provides
        @Singleton
        fun provideJson(): Json = Json {
            ignoreUnknownKeys = true //игнорировать неиспользуемые свойства из json
            coerceInputValues = true //если св-о ненул, а в ответе нулл - использовать значение по умолчанию
        }
        @Provides
        @Singleton
        fun provideConverter(
            json: Json
        ): Converter.Factory {
            return json.asConverterFactory(
                "application/json".toMediaType()
            ) //передать конвектор, который преобразует ответ в Json
        }
        @Provides
        @Singleton
        fun provideRetrofit(
            converter: Converter.Factory
        ): Retrofit {
            val baseUrl = BASE_URL //базовый url запроса
            return Retrofit.Builder() //сам объект ретрофита
                .baseUrl(baseUrl)
                .addConverterFactory(converter)
                .build()
        }
        @Provides
        @Singleton
        fun provideService(
            retrofit: Retrofit
        ): TTGamesApiService {
            return retrofit.create<TTGamesApiService>() //объект, который генерирует реализацию интерфейса
        }

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

        private const val BASE_URL = "https://raw.githubusercontent.com/RadimNurmamedov/Table_Time_Game/main/"
    }
}