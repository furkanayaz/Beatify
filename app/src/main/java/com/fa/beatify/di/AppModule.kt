package com.fa.beatify.di

import android.content.Context
import androidx.room.Room
import com.fa.beatify.apis.BeatifyRepo
import com.fa.beatify.apis.DeezerDao
import com.fa.beatify.apis.LikeDao
import com.fa.beatify.builders.RoomDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import com.fa.beatify.constants.ApiConstants.BASE_URL
import com.fa.beatify.utils.network.NetworkConnection
import com.fa.beatify.utils.repos.DateRepo
import com.fa.beatify.utils.repos.DurationRepo
import com.fa.beatify.utils.repos.ImageRepo
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDateRepo(): DateRepo = DateRepo()

    @Provides
    @Singleton
    fun provideDurationRepo(): DurationRepo = DurationRepo()

    @Provides
    @Singleton
    fun provideImageRepo(): ImageRepo = ImageRepo()

    @Provides
    @Singleton
    fun provideNetworkConnection(@ApplicationContext context: Context): NetworkConnection = NetworkConnection(context = context)

    @Provides
    @Singleton
    fun provideBeatifyRepo(deezerDao: DeezerDao, likeDao: LikeDao): BeatifyRepo =
        BeatifyRepo(deezerDao = deezerDao, likeDao = likeDao)

    @Provides
    @Singleton
    fun provideLikeApi(@ApplicationContext context: Context): LikeDao = Room.databaseBuilder(
        context = context.applicationContext, klass = RoomDB::class.java, name = "like.sqlite"
    ).createFromAsset(databaseFilePath = "like.sqlite").build().getDao()

    @Provides
    @Singleton
    fun provideDeezerApi(): DeezerDao =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build().create(DeezerDao::class.java)

}