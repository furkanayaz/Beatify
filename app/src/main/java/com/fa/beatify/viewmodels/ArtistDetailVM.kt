package com.fa.beatify.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fa.beatify.models.AlbumModel
import com.fa.beatify.repositories.ArtistDetailRepo

class ArtistDetailVM: ViewModel() {
    private val artistDetailRepo = ArtistDetailRepo()

    fun getAlbums(artistId: Int) = artistDetailRepo.getAlbums(artistId = artistId)

    fun getReleaseDate(releaseDate: String): String = artistDetailRepo.getReleaseDate(releaseDate = releaseDate)

    fun getAlbumModel(): MutableLiveData<List<AlbumModel>> = artistDetailRepo.getAlbumModel()

}