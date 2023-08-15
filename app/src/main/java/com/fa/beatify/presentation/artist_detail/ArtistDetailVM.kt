package com.fa.beatify.presentation.artist_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fa.beatify.data.response.BeatifyResponse
import com.fa.beatify.domain.models.Album
import com.fa.beatify.domain.remote.use_cases.AllAlbumsUseCase
import com.fa.beatify.utils.network.Connection
import com.fa.beatify.utils.network.NetworkConnection
import com.fa.beatify.utils.repos.DateRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArtistDetailVM(
    networkConnection: NetworkConnection,
    private val allAlbumsUseCase: AllAlbumsUseCase,
    private val dateRepo: DateRepo
) : ViewModel() {
    private var _connObserver: Flow<Connection.Status>? = null

    private val connObserver: Flow<Connection.Status> get() = _connObserver!!

    private var _albums: MutableStateFlow<BeatifyResponse<List<Album>>>? = null
    val albums: StateFlow<BeatifyResponse<List<Album>>>
        get() = _albums!!.asStateFlow()

    private var getAlbumsJob: Job? = null

    init {
        _connObserver = networkConnection.observe()
        _albums = MutableStateFlow(value = BeatifyResponse.Loading())
    }

    fun fetchData(artistId: Int) {
        viewModelScope.launch(context = Dispatchers.Unconfined) {
            connObserver.collect {
                when (it) {
                    Connection.Status.Available -> getAlbums(artistId = artistId)
                    Connection.Status.Losing -> _albums?.emit(value = BeatifyResponse.Loading())
                    Connection.Status.Unavailable, Connection.Status.Lost -> _albums?.emit(
                        value = BeatifyResponse.Failure(
                            code = 404
                        )
                    )
                }
            }
        }
    }

    private fun getAlbums(artistId: Int) {
        getAlbumsJob = viewModelScope.launch(context = Dispatchers.IO) {
            try {
                allAlbumsUseCase(artistId = artistId).also { albumList: List<Album> ->
                    _albums?.emit(
                        value = BeatifyResponse.Success(
                            data = albumList, code = 200
                        )
                    )
                }
            } catch (e: Exception) {
                _albums?.emit(value = BeatifyResponse.Failure(code = 404))
            }
        }
    }

    fun getReleaseDate(releaseDate: String?): String =
        dateRepo.getReleaseDate(releaseDate = releaseDate ?: "0")

    override fun onCleared() {
        super.onCleared()
        getAlbumsJob = null

        _connObserver = null
        _albums = null
    }

}