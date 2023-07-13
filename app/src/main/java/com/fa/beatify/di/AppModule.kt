package com.fa.beatify.di

import android.content.Context
import androidx.room.Room
import com.fa.beatify.data.remote_source.DeezerDao
import com.fa.beatify.data.local_source.LikeDao
import com.fa.beatify.data.local_source.RoomDB
import com.fa.beatify.domain.local_use_cases.AllLikesUseCase
import com.fa.beatify.domain.local_use_cases.DeleteLikeUseCase
import com.fa.beatify.domain.local_use_cases.InsertLikeUseCase
import com.fa.beatify.domain.remote_use_cases.AllAlbumsUseCase
import com.fa.beatify.domain.remote_use_cases.AllArtistsUseCase
import com.fa.beatify.domain.remote_use_cases.AllGenresUseCase
import com.fa.beatify.domain.remote_use_cases.AllTracksUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import com.fa.beatify.utils.constants.ApiConstants.BASE_URL
import com.fa.beatify.utils.network.NetworkConnection
import com.fa.beatify.utils.repos.DateRepo
import com.fa.beatify.utils.repos.DurationRepo
import com.fa.beatify.utils.repos.ImageRepo
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
object AppModule {

    @[Provides Singleton]
    fun provideDateRepo(): DateRepo = DateRepo()

    @[Provides Singleton]
    fun provideDurationRepo(): DurationRepo = DurationRepo()

    @[Provides Singleton]
    fun provideImageRepo(): ImageRepo = ImageRepo()

    @[Provides Singleton]
    fun provideNetworkConnection(@ApplicationContext context: Context): NetworkConnection =
        NetworkConnection(context = context)

    @[Provides Singleton]
    fun provideDeezerApi(): DeezerDao =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build().create(DeezerDao::class.java)

    @[Provides Singleton]
    fun provideLikeApi(@ApplicationContext context: Context): LikeDao = Room.databaseBuilder(
        context = context.applicationContext, klass = RoomDB::class.java, name = "like.sqlite"
    ).createFromAsset(databaseFilePath = "like.sqlite").build().getDao()

    @[Provides Singleton]
    fun provideAllLikes(likeDao: LikeDao): AllLikesUseCase = AllLikesUseCase(likeDao = likeDao)

    @[Provides Singleton]
    fun provideDeleteLike(likeDao: LikeDao): DeleteLikeUseCase = DeleteLikeUseCase(likeDao = likeDao)

    @[Provides Singleton]
    fun provideInsertLike(likeDao: LikeDao): InsertLikeUseCase = InsertLikeUseCase(likeDao = likeDao)

    @[Provides Singleton]
    fun provideAllAlbums(deezerDao: DeezerDao): AllAlbumsUseCase = AllAlbumsUseCase(deezerDao = deezerDao)

    @[Provides Singleton]
    fun provideAllArtists(deezerDao: DeezerDao): AllArtistsUseCase = AllArtistsUseCase(deezerDao = deezerDao)

    @[Provides Singleton]
    fun provideAllGenres(deezerDao: DeezerDao): AllGenresUseCase = AllGenresUseCase(deezerDao = deezerDao)

    @[Provides Singleton]
    fun provideAllTracks(deezerDao: DeezerDao): AllTracksUseCase = AllTracksUseCase(deezerDao = deezerDao)

}