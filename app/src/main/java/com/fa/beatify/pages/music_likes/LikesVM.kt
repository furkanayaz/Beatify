package com.fa.beatify.pages.music_likes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fa.beatify.apis.BeatifyRepo
import com.fa.beatify.entities.LikeEntities
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikesVM @Inject constructor(private val beatifyRepo: BeatifyRepo) : ViewModel() {
    private val likesData = MutableLiveData<List<LikeEntities>>()

    fun allLikes() {
        viewModelScope.launch {
            likesData.postValue(beatifyRepo.allLikes())
        }
    }

    fun deleteLike(like: LikeEntities) {
        viewModelScope.launch {
            beatifyRepo.deleteLike(like = like)
        }
    }

    fun getLikesData(): MutableLiveData<List<LikeEntities>> = likesData

}