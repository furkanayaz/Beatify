package com.fa.beatify.domain.local.use_cases

import com.fa.beatify.data.local.services.LikeDataSource
import com.fa.beatify.data.models.Like

class InsertLikeUseCase(
    private val likeDataSource: LikeDataSource
) {

    suspend operator fun invoke(like: Like): Unit = likeDataSource.insertLike(like = like)

}