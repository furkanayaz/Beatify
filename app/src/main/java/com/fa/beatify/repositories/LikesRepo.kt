package com.fa.beatify.repositories

import android.media.MediaPlayer
import androidx.lifecycle.MutableLiveData
import com.fa.beatify.entities.LikeEntities
import com.fa.beatify.rooms.RoomDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class LikesRepo {
    private val likesData = MutableLiveData<List<LikeEntities>>()
    private var mediaPlayer: MediaPlayer? = null

    init {
        getLikes()
        mediaPlayer = MediaPlayer()
    }

    fun getLikes() {
        CoroutineScope(context = Dispatchers.Main).launch {
            likesData.postValue(RoomDB.INSTANCE?.getDao()?.getLikes())

            cancel()
        }
    }

    fun deleteLike(like: LikeEntities) {
        CoroutineScope(context = Dispatchers.Main).launch {
            RoomDB.INSTANCE?.getDao()?.deleteLike(like = like)
            getLikes()
            cancel()
        }
    }

    fun playMusic(url: String) {
        mediaPlayer!!.setDataSource(url)
        mediaPlayer!!.prepare()
        mediaPlayer!!.start()
    }

    fun stopMusic() {
        mediaPlayer!!.stop()
        mediaPlayer!!.reset()
    }

    fun getLikesData(): MutableLiveData<List<LikeEntities>> = likesData

}