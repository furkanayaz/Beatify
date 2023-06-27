package com.fa.beatify.pages.artists

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fa.beatify.apis.BeatifyRepo
import com.fa.beatify.apis.BeatifyResponse
import com.fa.beatify.models.Artist
import com.fa.beatify.utils.network.Connection
import com.fa.beatify.utils.network.NetworkConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistsVM @Inject constructor(networkConnection: NetworkConnection, private val beatifyRepo: BeatifyRepo) : ViewModel() {
    private val _connObserver: Flow<Connection.Status> = networkConnection.observe()
    private val _artists = MutableLiveData<BeatifyResponse<Artist>>()
    val artists: MutableLiveData<BeatifyResponse<Artist>>
        get() = _artists

    fun fetchData(genreId: Int) {
        _artists.postValue(BeatifyResponse.Loading())

        viewModelScope.launch {
            _connObserver.collect {
                when(it) {
                    Connection.Status.Available -> allArtists(genreId = genreId)
                    Connection.Status.Losing -> _artists.postValue(BeatifyResponse.Loading())
                    Connection.Status.Unavailable, Connection.Status.Lost -> _artists.postValue(BeatifyResponse.Failure(code = 404))
                }
            }
        }
    }

    private fun allArtists(genreId: Int) {
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