package com.fa.beatify.domain.local.use_cases

import com.fa.beatify.data.local.services.LikeDao
import com.fa.beatify.data.models.Like
import kotlinx.coroutines.flow.Flow

class AllLikesUseCase(
    private val likeDao: LikeDao
) {

    suspend operator fun invoke(): Flow<List<Like>> = likeDao.getLikes()

}