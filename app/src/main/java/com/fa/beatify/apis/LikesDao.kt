package com.fa.beatify.apis

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.fa.beatify.entities.LikeEntities

@Dao
interface LikesDao {

    @Query("SELECT * FROM likes")
    suspend fun getLikes(): List<LikeEntities>

    @Insert
    suspend fun insertLike(like: LikeEntities)

    @Delete
    suspend fun deleteLike(like: LikeEntities)

}