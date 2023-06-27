package com.fa.beatify.pages.album_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fa.beatify.apis.BeatifyRepo
import com.fa.beatify.apis.BeatifyResponse
import com.fa.beatify.entities.LikeEntities
import com.fa.beatify.models.Track
import com.fa.beatify.utils.network.Connection
import com.fa.beatify.utils.network.NetworkConnection
import com.fa.beatify.utils.repos.DurationRepo
import com.fa.beatify.utils.repos.ImageRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumDetailVM @Inject constructor(
    networkConnection: NetworkConnection,
    private val beatifyRepo: BeatifyRepo,
    private val imageRepo: ImageRepo,
    private val durationRepo: DurationRepo
) : ViewModel() {
    private val _connObserver: Flow<Connection.Status> = networkConnection.observe()
    private val _trackList = MutableLiveData<BeatifyResponse<Track>>()
    val trackList: MutableLiveData<BeatifyResponse<Track>>
        get() = _trackList

    fun fetchData(albumId: Int) {
        _trackList.postValue(BeatifyResponse.Loading())

        viewModelScope.launch {
            _connObserver.collect {
                when (it) {
                    Connection.Status.Available -> getTracks(albumId = albumId)
                    Connection.Status.Losing -> _trackList.postValue(BeatifyResponse.Loading())
                    Connection.Status.Unavailable, Connection.Status.Lost -> _trackList.postValue(
                        BeatifyResponse.Failure(code = 404)
                    )
                }
            }
        }
    }

    private fun getTracks(albumId: Int) {
        viewModelScope.launch {
            try {
                val allTracks: Track? = beatifyRepo.allTracks(albumId = albumId)
                allTracks?.let { track: Track ->
                    _trackList.postValue(BeatifyResponse.Success(data = track, code = 200))
                } ?: _trackList.postValue(BeatifyResponse.Failure(code = 204))
            } catch (e: Exception) {
                _trackList.postValue(BeatifyResponse.Failure(code = 404))
            }
        }
    }

    fun getImage(md5Image: String?): String = imageRepo.getImage(md5Image = md5Image ?: "")

    fun getDuration(durationInSeconds: Int?): String =
        durationRepo.getDuration(durationInSeconds = durationInSeconds ?: 0)

    fun insertLike(like: LikeEntities) {
        viewModelScope.launch {
            beatifyRepo.insertLike(like = like)
        }
    }

    fun deleteLike(like: LikeEntities) {
        viewModelScope.launch {
            beatifyRepo.deleteLike(like = like)
        }
    }
}