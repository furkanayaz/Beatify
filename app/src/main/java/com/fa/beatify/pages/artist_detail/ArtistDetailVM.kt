package com.fa.beatify.pages.artist_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fa.beatify.models.AlbumModel

class ArtistDetailVM: ViewModel() {
    private val artistDetailRepo = ArtistDetailRepo()

    fun getAlbums(artistId: Int) = artistDetailRepo.getAlbums(artistId = artistId)

    fun getReleaseDate(releaseDate: String): String = artistDetailRepo.getReleaseDate(releaseDate = releaseDate)

    fun getAlbumModel(): MutableLiveData<List<AlbumModel>> = artistDetailRepo.getAlbumModel()

}