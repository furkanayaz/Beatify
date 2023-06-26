package com.fa.beatify.pages.artist_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fa.beatify.apis.BeatifyRepo
import com.fa.beatify.apis.BeatifyResponse
import com.fa.beatify.models.Album
import com.fa.beatify.utils.repos.DateRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistDetailVM @Inject constructor(
    private val beatifyRepo: BeatifyRepo, private val dateRepo: DateRepo
) : ViewModel() {
    private val _albumModel = MutableLiveData<BeatifyResponse<Album>>()
    val albumModel: MutableLiveData<BeatifyResponse<Album>>
        get() = _albumModel

    fun getAlbums(artistId: Int) {
        viewModelScope.launch {
            try {
                val allAlbums = beatifyRepo.allAlbums(artistId = artistId)
                allAlbums?.let { album: Album ->
                    _albumModel.postValue(
                        BeatifyResponse.Success(
                            data = album, code = 200
                        )
                    )
                } ?: _albumModel.postValue(BeatifyResponse.Failure(code = 204))
            } catch (e: Exception) {
                _albumModel.postValue(BeatifyResponse.Failure(code = 404))
            }
        }
    }

    fun getReleaseDate(releaseDate: String?): String =
        dateRepo.getReleaseDate(releaseDate = releaseDate ?: "0")
}