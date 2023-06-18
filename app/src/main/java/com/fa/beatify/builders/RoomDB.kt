package com.fa.beatify.builders

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fa.beatify.entities.LikeEntities
import com.fa.beatify.apis.LikesDao

@Database(entities = [LikeEntities::class], version = 1, exportSchema = false)
abstract class RoomDB: RoomDatabase() {
    abstract fun getDao(): LikesDao

    companion object {
        var INSTANCE: RoomDB? = null

        fun accessDatabase(context: Context) {
            synchronized(RoomDB::class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context = context.applicationContext,
                        klass = RoomDB::class.java,
                        name = "likes.sqlite"
                    ).createFromAsset(databaseFilePath = "likes.sqlite").build()
                }
            }
        }

    }

}