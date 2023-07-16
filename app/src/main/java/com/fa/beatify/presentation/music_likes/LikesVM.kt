package com.fa.beatify.presentation.music_likes

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fa.beatify.domain.local.use_cases.AllLikesUseCase
import com.fa.beatify.domain.local.use_cases.DeleteLikeUseCase
import com.fa.beatify.data.models.Like
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LikesVM(
    private val allLikesUseCase: AllLikesUseCase,
    private val deleteLikeUseCase: DeleteLikeUseCase
) : ViewModel() {
    private val _likesData = MutableLiveData<List<Like>>()
    val likesData: MutableLiveData<List<Like>>
        get() = _likesData

    @SuppressLint("NullSafeMutableLiveData")
    fun allLikes() {
        viewModelScope.launch(context = Dispatchers.IO) {
            _likesData.postValue(allLikesUseCase())
        }
    }

    fun deleteLike(like: Like) {
        viewModelScope.launch(context = Dispatchers.IO) {
            deleteLikeUseCase(like = like)
            allLikes()
        }
    }
}