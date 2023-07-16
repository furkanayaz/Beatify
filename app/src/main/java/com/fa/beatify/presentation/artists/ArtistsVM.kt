package com.fa.beatify.presentation.artists

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fa.beatify.data.response.BeatifyResponse
import com.fa.beatify.domain.models.Artist
import com.fa.beatify.domain.remote.use_cases.AllArtistsUseCase
import com.fa.beatify.utils.network.Connection
import com.fa.beatify.utils.network.NetworkConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ArtistsVM(
    networkConnection: NetworkConnection,
    private val allArtistsUseCase: AllArtistsUseCase
) : ViewModel() {
    private val _connObserver: Flow<Connection.Status> = networkConnection.observe()
    private val _artists = MutableLiveData<BeatifyResponse<List<Artist>>>()
    val artists: MutableLiveData<BeatifyResponse<List<Artist>>>
        get() = _artists

    fun fetchData(genreId: Int) {
        _artists.postValue(BeatifyResponse.Loading())

        viewModelScope.launch(context = Dispatchers.Unconfined) {
            _connObserver.collect {
                when (it) {
                    Connection.Status.Available -> allArtists(genreId = genreId)
                    Connection.Status.Losing -> _artists.postValue(BeatifyResponse.Loading())
                    Connection.Status.Unavailable, Connection.Status.Lost -> _artists.postValue(
                        BeatifyResponse.Failure(code = 404)
                    )
                }
            }
        }
    }

    private fun allArtists(genreId: Int) {
        viewModelScope.launch(context = Dispatchers.IO) {
            try {
                allArtistsUseCase(genreId = genreId).also { artistList: List<Artist> ->
                    _artists.postValue(BeatifyResponse.Success(data = artistList, code = 200))
                }
            } catch (e: Exception) {
                _artists.postValue(BeatifyResponse.Failure(code = 404))
            }
        }
    }
}