package com.fa.beatify.pages.album_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fa.beatify.apis.BeatifyRepo
import com.fa.beatify.entities.LikeEntities
import com.fa.beatify.models.TrackModel
import com.fa.beatify.utils.DurationRepo
import com.fa.beatify.utils.ImageRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumDetailVM @Inject constructor(private val beatifyRepo: BeatifyRepo, private val imageRepo: ImageRepo, private val durationRepo: DurationRepo): ViewModel() {
    private val _trackList = MutableLiveData<List<TrackModel>>()
    val trackList: MutableLiveData<List<TrackModel>>
        get() = _trackList
    fun getTracks(albumId: Int) {
        viewModelScope.launch {
            _trackList.postValue(beatifyRepo.allTracks(albumId = albumId))
        }
    }

    fun getImage(md5Image: String?): String = imageRepo.getImage(md5Image = md5Image?: "")

    fun getDuration(durationInSeconds: Int?): String = durationRepo.getDuration(durationInSeconds = durationInSeconds?: 0)

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