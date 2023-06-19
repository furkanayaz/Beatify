package com.fa.beatify.pages.artists

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fa.beatify.apis.BeatifyRepo
import com.fa.beatify.models.ArtistModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistsVM @Inject constructor(private val beatifyRepo: BeatifyRepo): ViewModel() {
    private val artists = MutableLiveData<List<ArtistModel>>()

    fun allArtists(genreId: Int) {
        viewModelScope.launch {
            artists.postValue(beatifyRepo.allArtists(genreId = genreId))
        }
    }

    fun getArtists(): MutableLiveData<List<ArtistModel>> = artists
}