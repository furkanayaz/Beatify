package com.fa.beatify.domain.local_use_cases

import com.fa.beatify.data.local_source.LikeDao
import com.fa.beatify.data.models.Like
import javax.inject.Inject

class AllLikesUseCase @Inject constructor(
    private val likeDao: LikeDao
) {

    suspend operator fun invoke(): List<Like> = likeDao.getLikes()

}