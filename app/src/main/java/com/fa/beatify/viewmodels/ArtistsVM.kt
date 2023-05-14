package com.fa.beatify.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fa.beatify.models.Artist
import com.fa.beatify.models.ArtistModel
import com.fa.beatify.repositories.ArtistsRepo

class ArtistsVM: ViewModel() {
    private val artistsRepo = ArtistsRepo()

    fun allArtists(genreId: Int) = artistsRepo.allArtists(genreId = genreId)

    fun getArtists(): MutableLiveData<List<ArtistModel>> = artistsRepo.getArtists()
}