package com.fa.beatify.domain.local.use_cases

import com.fa.beatify.data.local.services.LikeDao
import com.fa.beatify.data.models.Like

class DeleteLikeUseCase(
    private val likeDao: LikeDao
) {

    suspend operator fun invoke(like: Like): Unit = likeDao.deleteLike(like = like)

}