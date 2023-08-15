package com.fa.beatify.data.local.services

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.fa.beatify.data.models.Like
import com.fa.beatify.data.local.Endpoints.GET_LIKES

@Dao
interface LikeDataSource {
    @Query(GET_LIKES)
    fun getLikes(): LiveData<List<Like>>

    @Insert
    suspend fun insertLike(like: Like)

    @Delete
    suspend fun deleteLike(like: Like)
}