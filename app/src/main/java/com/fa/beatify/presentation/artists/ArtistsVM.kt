package com.fa.beatify.presentation.artists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fa.beatify.data.response.Response
import com.fa.beatify.domain.models.Artist
import com.fa.beatify.domain.remote.use_cases.AllArtistsUseCase
import com.fa.beatify.utils.network.Connection
import com.fa.beatify.utils.network.NetworkConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArtistsVM(
    networkConnection: NetworkConnection, private val allArtistsUseCase: AllArtistsUseCase
) : ViewModel() {
    private var _connObserver: Flow<Connection.Status>? = null

    private val connObserver: Flow<Connection.Status> get() = _connObserver!!

    private var _artists: MutableStateFlow<Response<List<Artist>>>? = null
    val artists: StateFlow<Response<List<Artist>>>
        get() = _artists!!.asStateFlow()

    private var allArtistsJob: Job? = null

    init {
        _connObserver = networkConnection.observe()
        _artists = MutableStateFlow(value = Response.Loading)
    }

    fun fetchData(genreId: Int) {
        viewModelScope.launch(context = Dispatchers.Unconfined) {
            connObserver.collect {
                when (it) {
                    Connection.Status.Available -> allArtists(genreId = genreId)
                    Connection.Status.Losing -> _artists?.emit(value = Response.Loading)
                    Connection.Status.Unavailable, Connection.Status.Lost -> _artists?.emit(
                        value = Response.Failure(code = 404)
                    )
                }
            }
        }
    }

    private fun allArtists(genreId: Int) {
        allArtistsJob = viewModelScope.launch(context = Dispatchers.IO) {
            try {
                allArtistsUseCase(genreId = genreId).also { artistList: List<Artist> ->
                    _artists?.emit(value = Response.Success(data = artistList, code = 200))
                }
            } catch (e: Exception) {
                _artists?.emit(value = Response.Failure(code = 404))
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