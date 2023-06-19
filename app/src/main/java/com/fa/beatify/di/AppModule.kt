package com.fa.beatify.di

import com.fa.beatify.apis.BeatifyRepo
import com.fa.beatify.apis.DeezerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import com.fa.beatify.constants.ApiConstants.BASE_URL
import com.fa.beatify.utils.DateRepo
import com.fa.beatify.utils.DurationRepo
import com.fa.beatify.utils.ImageRepo
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
    fun provideBeatifyRepo(deezerDao: DeezerDao): BeatifyRepo = BeatifyRepo(deezerDao = deezerDao)

    @Provides
    @Singleton
    fun provideDeezerApi(): DeezerDao = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DeezerDao::class.java)

}