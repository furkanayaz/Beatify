package com.fa.beatify.presentation.artist_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fa.beatify.data.response.BeatifyResponse
import com.fa.beatify.domain.models.Album
import com.fa.beatify.domain.remote_use_cases.AllAlbumsUseCase
import com.fa.beatify.utils.network.Connection
import com.fa.beatify.utils.network.NetworkConnection
import com.fa.beatify.utils.repos.DateRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistDetailVM @Inject constructor(
    networkConnection: NetworkConnection,
    private val allAlbumsUseCase: AllAlbumsUseCase, private val dateRepo: DateRepo
) : ViewModel() {
    private val _connObserver: Flow<Connection.Status> = networkConnection.observe()
    private val _albums = MutableLiveData<BeatifyResponse<List<Album>>>()
    val albums: MutableLiveData<BeatifyResponse<List<Album>>>
        get() = _albums

    fun fetchData(artistId: Int) {
        _albums.postValue(BeatifyResponse.Loading())

        viewModelScope.launch(context = Dispatchers.Unconfined) {
            _connObserver.collect {
                when (it) {
                    Connection.Status.Available -> getAlbums(artistId = artistId)
                    Connection.Status.Losing -> _albums.postValue(BeatifyResponse.Loading())
                    Connection.Status.Unavailable, Connection.Status.Lost -> _albums.postValue(
                        BeatifyResponse.Failure(code = 404)
                    )
                }
            }
        }
    }

    private fun getAlbums(artistId: Int) {
        viewModelScope.launch(context = Dispatchers.IO) {
            try {
                allAlbumsUseCase(artistId = artistId).also { albumList: List<Album> ->
                    _albums.postValue(
                        BeatifyResponse.Success(
                            data = albumList, code = 200
                        )
                    )
                }
            } catch (e: Exception) {
                _albums.postValue(BeatifyResponse.Failure(code = 404))
            }
        }
    }

    fun getReleaseDate(releaseDate: String?): String =
        dateRepo.getReleaseDate(releaseDate = releaseDate ?: "0")
}