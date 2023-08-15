package com.fa.beatify.presentation.music_likes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fa.beatify.domain.local.use_cases.AllLikesUseCase
import com.fa.beatify.domain.local.use_cases.DeleteLikeUseCase
import com.fa.beatify.data.models.Like
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class LikesVM(
    private val allLikesUseCase: AllLikesUseCase,
    private val deleteLikeUseCase: DeleteLikeUseCase
) : ViewModel() {
    private var _likesData: Flow<List<Like>>? = null
    val likesData: Flow<List<Like>>
        get() = _likesData!!

    private var allLikesJob: Job? = null
    private var deleteLikeJob: Job? = null

    init {
        allLikes()
    }

    private fun allLikes() {
        allLikesJob = viewModelScope.launch(context = Dispatchers.IO) {
            _likesData = allLikesUseCase()
        }
    }

    fun deleteLike(like: Like) {
        deleteLikeJob = viewModelScope.launch(context = Dispatchers.IO) {
            deleteLikeUseCase(like = like)
        }
    }

    override fun onCleared() {
        super.onCleared()
        allLikesJob = null
        deleteLikeJob = null

        _likesData = null
    }

}