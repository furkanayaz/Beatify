package com.fa.beatify.domain.local.use_cases

import com.fa.beatify.data.local.services.LikeDao
import com.fa.beatify.data.models.Like

class AllLikesUseCase(
    private val likeDao: LikeDao
) {

    suspend operator fun invoke(): List<Like> = likeDao.getLikes()

}