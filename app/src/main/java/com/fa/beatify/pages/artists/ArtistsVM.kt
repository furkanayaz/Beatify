package com.fa.beatify.pages.artists

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fa.beatify.models.ArtistModel

class ArtistsVM: ViewModel() {
    private val artistsRepo = ArtistsRepo()

    fun allArtists(genreId: Int) = artistsRepo.allArtists(genreId = genreId)

    fun getArtists(): MutableLiveData<List<ArtistModel>> = artistsRepo.getArtists()
}