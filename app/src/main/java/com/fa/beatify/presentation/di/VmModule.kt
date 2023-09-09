package com.fa.beatify.presentation.di

import com.fa.beatify.presentation.activity.MainActivityVM
import com.fa.beatify.presentation.album_detail.AlbumDetailVM
import com.fa.beatify.presentation.artist_detail.ArtistDetailVM
import com.fa.beatify.presentation.artists.ArtistsVM
import com.fa.beatify.presentation.music_categories.MusicCategoriesVM
import com.fa.beatify.presentation.music_likes.LikesVM
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

val vmModule: Module = module {

    viewModelOf(::MainActivityVM)
    viewModelOf(::LikesVM)
    viewModelOf(::MusicCategoriesVM)
    viewModelOf(::ArtistsVM)
    viewModelOf(::ArtistDetailVM)
    viewModelOf(::AlbumDetailVM)

}