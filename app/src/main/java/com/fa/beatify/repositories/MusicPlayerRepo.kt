package com.fa.beatify.repositories

import android.media.MediaPlayer
import com.fa.beatify.controllers.MusicController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class MusicPlayerRepo {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var timerJob: Job

    private val musicTimer: Flow<Int> = flow {
        var currentPos = 0

        while (true) {
            emit(value = currentPos)
            delay(timeMillis = 1000L)
            currentPos++
        }
    }

    fun playMusic(url: String) {
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepare()
        mediaPlayer.setOnPreparedListener { mp: MediaPlayer ->
            mp.start()
            MusicController.playingController.value = true
            collectMusicTimer(duration = mediaPlayer.duration.firstTwoDigits())
        }
    }

    fun stopMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.release()
            MusicController.playingController.value = false
            timerJob.cancel()
        }
    }

    private fun collectMusicTimer(duration: Int) {
        timerJob = CoroutineScope(context = Dispatchers.Default).launch {
            musicTimer.collect(collector = { pos: Int ->
                if (pos == duration) {
                    timerJob.cancel()
                    MusicController.playingController.value = false
                }
            })
        }
    }

    fun getPlayingController(): MutableStateFlow<Boolean> = MusicController.playingController

    private fun Int.firstTwoDigits(): Int {
        return (this.toString().substring(startIndex = 0, endIndex = 2).toInt() + 1)
    }

}