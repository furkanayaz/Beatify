package com.fa.beatify.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fa.beatify.data.local.services.LikeDataSource
import com.fa.beatify.data.models.Like

@Database(entities = [Like::class], version = 1, exportSchema = false)
abstract class RoomDB : RoomDatabase() {
    abstract fun getDao(): LikeDataSource
}