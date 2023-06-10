package com.fa.beatify.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.fa.beatify.controllers.MusicController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MusicPlayer: Service() {
    private lateinit var mediaPlayer: MediaPlayer
    private var timerJob: Job? = null

    override fun onCreate() {
        mediaPlayer = MediaPlayer()
        super.onCreate()
    }

    override fun onDestroy() {
        destroyAllProcess()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val url: String? = intent?.getStringExtra("url")

        url?.let { locUrl ->
            playMusic(url = locUrl)
        }

        return START_NOT_STICKY
    }

    private val musicTimer: Flow<Int> = flow {
        var currentPos = 0

        while (true) {
            emit(value = currentPos)
            delay(timeMillis = 1000L)
            currentPos++
        }
    }

    private fun playMusic(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepare()
        mediaPlayer.setOnPreparedListener { mp: MediaPlayer ->
            mp.start()
            MusicController.playingController.value = true
            collectMusicTimer(duration = mediaPlayer.duration.firstTwoDigits())
        }
    }

    private fun collectMusicTimer(duration: Int) {
        timerJob = CoroutineScope(context = Dispatchers.Default).launch {
            musicTimer.collect(collector = { pos: Int ->
                if (pos == duration) {
                    destroyAllProcess()
                    stopSelf()
                }
            })
        }
    }

    private fun destroyAllProcess() {
        mediaPlayer.release()
        MusicController.playingController.value = false
        timerJob?.cancel()
    }

    private fun Int.firstTwoDigits(): Int {
        return (this.toString().substring(startIndex = 0, endIndex = 2).toInt() + 1)
    }

}