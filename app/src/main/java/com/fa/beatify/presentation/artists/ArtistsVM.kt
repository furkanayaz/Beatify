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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ArtistsVM(
    networkConnection: NetworkConnection,
    private val allArtistsUseCase: AllArtistsUseCase
) : ViewModel() {
    private var _connObserver: Flow<Connection.Status>? = null

    private val connObserver: Flow<Connection.Status> get() = _connObserver!!

    private var _artists: MutableLiveData<BeatifyResponse<List<Artist>>>? = null
    val artists: MutableLiveData<BeatifyResponse<List<Artist>>>
        get() = _artists!!

    private var allArtistsJob: Job? = null

    init {
        _connObserver = networkConnection.observe()
        _artists = MutableLiveData<BeatifyResponse<List<Artist>>>()
    }

    fun fetchData(genreId: Int) {
        _artists?.postValue(BeatifyResponse.Loading())

        viewModelScope.launch(context = Dispatchers.Unconfined) {
            connObserver.collect {
                when (it) {
                    Connection.Status.Available -> allArtists(genreId = genreId)
                    Connection.Status.Losing -> _artists?.postValue(BeatifyResponse.Loading())
                    Connection.Status.Unavailable, Connection.Status.Lost -> _artists?.postValue(
                        BeatifyResponse.Failure(code = 404)
                    )
                }
            }
        }
    }

    private fun allArtists(genreId: Int) {
        allArtistsJob = viewModelScope.launch(context = Dispatchers.IO) {
            try {
                allArtistsUseCase(genreId = genreId).also { artistList: List<Artist> ->
                    _artists?.postValue(BeatifyResponse.Success(data = artistList, code = 200))
                }
            } catch (e: Exception) {
                _artists?.postValue(BeatifyResponse.Failure(code = 404))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        allArtistsJob = null

        _connObserver = null
        _artists = null
    }
}