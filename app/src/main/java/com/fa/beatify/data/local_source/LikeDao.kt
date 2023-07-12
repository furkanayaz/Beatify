package com.fa.beatify.data.local_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.fa.beatify.data.models.Like

@Dao
interface LikeDao {
    @Query("SELECT * FROM like")
    suspend fun getLikes(): List<Like>

    @Insert
    suspend fun insertLike(like: Like)

    @Delete
    suspend fun deleteLike(like: Like)
}