package com.fa.beatify.presentation.music_likes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fa.beatify.domain.local_use_cases.AllLikesUseCase
import com.fa.beatify.domain.local_use_cases.DeleteLikeUseCase
import com.fa.beatify.data.models.Like
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikesVM @Inject constructor(
    private val allLikesUseCase: AllLikesUseCase,
    private val deleteLikeUseCase: DeleteLikeUseCase
) : ViewModel() {
    private val _likesData = MutableLiveData<List<Like>>()
    val likesData: MutableLiveData<List<Like>>
        get() = _likesData

    fun allLikes() {
        viewModelScope.launch {
            _likesData.postValue(allLikesUseCase())
        }
    }

    fun deleteLike(like: Like) {
        viewModelScope.launch {
            deleteLikeUseCase(like = like)
            allLikes()
        }
    }
}