package com.fa.beatify.domain.local.use_cases

import androidx.lifecycle.LiveData
import com.fa.beatify.data.local.services.LikeDataSource
import com.fa.beatify.data.models.Like

class AllLikesUseCase(
    private val likeDataSource: LikeDataSource
) {

    suspend operator fun invoke(): LiveData<List<Like>> = likeDataSource.getLikes()

}