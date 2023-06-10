package com.fa.beatify.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fa.beatify.entities.LikeEntities
import com.fa.beatify.repositories.LikesRepo

class LikesVM: ViewModel() {
    private val likesRepo = LikesRepo()

    fun deleteLike(like: LikeEntities) = likesRepo.deleteLike(like = like)

    fun getLikesData(): MutableLiveData<List<LikeEntities>> = likesRepo.getLikesData()

}