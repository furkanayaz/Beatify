package com.fa.beatify.presentation.album_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fa.beatify.data.response.BeatifyResponse
import com.fa.beatify.data.models.Like
import com.fa.beatify.domain.local.use_cases.DeleteLikeUseCase
import com.fa.beatify.domain.local.use_cases.InsertLikeUseCase
import com.fa.beatify.domain.models.Track
import com.fa.beatify.domain.remote.use_cases.AllTracksUseCase
import com.fa.beatify.utils.network.Connection
import com.fa.beatify.utils.network.NetworkConnection
import com.fa.beatify.utils.repos.DurationRepo
import com.fa.beatify.utils.repos.ImageRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AlbumDetailVM(
    networkConnection: NetworkConnection,
    private val allTracksUseCase: AllTracksUseCase,
    private val insertLikeUseCase: InsertLikeUseCase,
    private val deleteLikeUseCase: DeleteLikeUseCase,
    private val imageRepo: ImageRepo,
    private val durationRepo: DurationRepo
) : ViewModel() {
    private var _connObserver: Flow<Connection.Status>? = null

    private val connObserver: Flow<Connection.Status> get() = _connObserver!!

    private var _tracks: MutableLiveData<BeatifyResponse<List<Track>>>? = null
    val tracks: MutableLiveData<BeatifyResponse<List<Track>>>
        get() = _tracks!!

    private var getTracksJob: Job? = null
    private var insertLikeJob: Job? = null
    private var deleteLikeJob: Job? = null

    init {
        _connObserver = networkConnection.observe()
        _tracks = MutableLiveData<BeatifyResponse<List<Track>>>()
    }

    fun fetchData(albumId: Int) {
        _tracks?.postValue(BeatifyResponse.Loading())

        viewModelScope.launch(context = Dispatchers.Unconfined) {
            connObserver.collect {
                when (it) {
                    Connection.Status.Available -> getTracks(albumId = albumId)
                    Connection.Status.Losing -> _tracks?.postValue(BeatifyResponse.Loading())
                    Connection.Status.Unavailable, Connection.Status.Lost -> _tracks?.postValue(
                        BeatifyResponse.Failure(code = 404)
                    )
                }
            }
        }
    }

    private fun getTracks(albumId: Int) {
        getTracksJob = viewModelScope.launch(context = Dispatchers.IO) {
            try {
                allTracksUseCase(albumId = albumId).also { trackList: List<Track> ->
                    _tracks?.postValue(BeatifyResponse.Success(data = trackList, code = 200))
                }
            } catch (e: Exception) {
                _tracks?.postValue(BeatifyResponse.Failure(code = 404))
            }
        }
    }

    fun getImage(md5Image: String?): String = imageRepo.getImage(md5Image ?: "")

    fun getDuration(durationInSeconds: Int?): String =
        durationRepo.getDuration(durationInSeconds = durationInSeconds ?: 0)

    fun insertLike(like: Like) {
        insertLikeJob = viewModelScope.launch(context = Dispatchers.IO) {
            insertLikeUseCase(like = like)
        }
    }

    fun deleteLike(like: Like) {
        deleteLikeJob = viewModelScope.launch(context = Dispatchers.IO) {
            deleteLikeUseCase(like = like)
        }
    }

    override fun onCleared() {
        super.onCleared()
        getTracksJob = null
        insertLikeJob = null
        deleteLikeJob = null

        _connObserver = null
        _tracks = null
    }

}