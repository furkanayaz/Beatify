package com.fa.beatify.presentation.di

import com.fa.beatify.presentation.activity.MainActivityVM
import com.fa.beatify.presentation.album_detail.AlbumDetailVM
import com.fa.beatify.presentation.artist_detail.ArtistDetailVM
import com.fa.beatify.presentation.artists.ArtistsVM
import com.fa.beatify.presentation.music_categories.MusicCategoriesVM
import com.fa.beatify.presentation.music_likes.LikesVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val vmModule: Module = module {

    viewModel {
        MainActivityVM()
    }

    viewModel {
        LikesVM(
            allLikesUseCase = get(),
            deleteLikeUseCase = get()
        )
    }

    viewModel {
        MusicCategoriesVM(
            networkConnection = get(),
            allGenresUseCase = get()
        )
    }

    viewModel {
        ArtistsVM(
            networkConnection = get(),
            allArtistsUseCase = get()
        )
    }

    viewModel {
        ArtistDetailVM(
            networkConnection = get(),
            allAlbumsUseCase = get(),
            dateRepo = get()
        )
    }

    viewModel {
        AlbumDetailVM(
            networkConnection = get(),
            allTracksUseCase = get(),
            insertLikeUseCase = get(),
            deleteLikeUseCase = get(),
            imageRepo = get(),
            durationRepo = get()
        )
    }

}