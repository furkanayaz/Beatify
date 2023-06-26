package com.fa.beatify.pages.artists

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fa.beatify.apis.BeatifyRepo
import com.fa.beatify.apis.BeatifyResponse
import com.fa.beatify.models.Artist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistsVM @Inject constructor(private val beatifyRepo: BeatifyRepo) : ViewModel() {
    private val _artists = MutableLiveData<BeatifyResponse<Artist>>()
    val artists: MutableLiveData<BeatifyResponse<Artist>>
        get() = _artists

    fun allArtists(genreId: Int) {
        viewModelScope.launch {
            try {
                val artists = beatifyRepo.allArtists(genreId = genreId)
                artists?.let { artist: Artist ->
                    _artists.postValue(BeatifyResponse.Success(data = artist, code = 200))
                } ?: _artists.postValue(BeatifyResponse.Failure(code = 204))
            } catch (e: Exception) {
                _artists.postValue(BeatifyResponse.Failure(code = 404))
            }
        }
    }
}