package com.fa.beatify.repositories

import android.media.MediaPlayer

class MusicPlayerRepo {
    private var mediaPlayer: MediaPlayer? = null

    init {
        mediaPlayer = MediaPlayer()
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

}