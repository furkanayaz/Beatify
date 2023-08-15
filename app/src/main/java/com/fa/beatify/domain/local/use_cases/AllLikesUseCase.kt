package com.fa.beatify.domain.local.use_cases

import com.fa.beatify.data.local.services.LikeDataSource
import com.fa.beatify.data.models.Like
import kotlinx.coroutines.flow.Flow

class AllLikesUseCase(
    private val likeDataSource: LikeDataSource
) {

    operator fun invoke(): Flow<List<Like>> = likeDataSource.getLikes()

}