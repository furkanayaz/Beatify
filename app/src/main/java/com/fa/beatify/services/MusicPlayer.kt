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
    private var timerJob: Job? = null

    override fun onCreate() {
        MusicController.mediaPlayer = MediaPlayer()
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

    private fun playMusic(url: String) {
        MusicController.apply {
            mediaPlayer?.let { player: MediaPlayer ->
                player.setDataSource(url)
                player.prepare()
                player.setOnPreparedListener { mp: MediaPlayer ->
                    mp.start()
                    trackingController.value = true
                    collectMusicTimer(duration = player.duration.firstTwoDigits())
                }
            }
        }
    }

    private val musicTimer: Flow<Int> = flow {
        var currentPos = 0

        while (true) {
            if (MusicController.mediaPlayer!!.isPlaying) {
                emit(value = currentPos)
                currentPos++
            }

            delay(timeMillis = 1000L)
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
        MusicController.mediaPlayer?.let { player: MediaPlayer ->
            player.release()
        }
        MusicController.trackingController.value = false
        timerJob?.cancel()
    }

    private fun Int.firstTwoDigits(): Int {
        return (this.toString().substring(startIndex = 0, endIndex = 2).toInt() + 1)
    }

}