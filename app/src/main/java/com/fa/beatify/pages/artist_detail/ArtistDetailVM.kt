package com.fa.beatify.pages.artist_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fa.beatify.apis.BeatifyRepo
import com.fa.beatify.models.AlbumModel
import com.fa.beatify.utils.DateRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistDetailVM @Inject constructor(private val beatifyRepo: BeatifyRepo, private val dateRepo: DateRepo): ViewModel() {
    private val albumModel = MutableLiveData<List<AlbumModel>>()

    fun getAlbums(artistId: Int) {
        viewModelScope.launch {
            albumModel.postValue(beatifyRepo.allAlbums(artistId = artistId))
        }
    }

    fun getReleaseDate(releaseDate: String?): String = dateRepo.getReleaseDate(releaseDate = releaseDate?: "0")

    fun getAlbumModel(): MutableLiveData<List<AlbumModel>> = albumModel

}