package com.fa.beatify.builders

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fa.beatify.entities.LikeEntities
import com.fa.beatify.apis.LikeDao

@Database(entities = [LikeEntities::class], version = 1, exportSchema = false)
abstract class RoomDB : RoomDatabase() {
    abstract fun getDao(): LikeDao
}