package com.fa.beatify.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.fa.beatify.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import com.fa.beatify.constants.MusicConstants.mediaPlayer
import com.fa.beatify.constants.MusicConstants.trackingController
import com.fa.beatify.constants.MusicConstants.playMusic
import com.fa.beatify.constants.NotificationConstants.CHANNEL_ID
import com.fa.beatify.constants.NotificationConstants.CHANNEL_NAME
import com.fa.beatify.constants.NotificationConstants.NOTIFICATION_ID
import com.fa.beatify.models.PlayMusic

class MusicPlayer: Service() {
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
            showNotification()
        }

        return START_NOT_STICKY
    }

    private fun playMusic(url: String) {
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

    private val musicTimer: Flow<Int> = flow {
        var currentPos = 0

        while (true) {
            if (mediaPlayer!!.isPlaying) {
                emit(value = currentPos)
                currentPos++
            }

            delay(timeMillis = 1000L)
        }
    }

    private fun showNotification() {
        playMusic?.let { playMusic: PlayMusic ->
            val manager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val builder: NotificationCompat.Builder?

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel: NotificationChannel? = manager.getNotificationChannel(CHANNEL_ID)

                if (channel == null) {
                    val tempChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
                    manager.createNotificationChannel(tempChannel)
                }

                builder = NotificationCompat.Builder(this, CHANNEL_ID)
                builder.setAutoCancel(false)
                builder.setSmallIcon(R.mipmap.light_icon)
                builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                builder.setContentTitle(playMusic.artistName)
                builder.setContentText("${playMusic.albumName} - ${playMusic.musicName}")

            }else {
                builder = NotificationCompat.Builder(this)
                builder.setAutoCancel(false)
                builder.setSmallIcon(R.mipmap.light_icon)
                builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                builder.setContentTitle(playMusic.artistName)
                builder.setContentText("${playMusic.albumName} - ${playMusic.musicName}")
            }

            startForeground(NOTIFICATION_ID, builder.build())
        }
    }

    private fun collectMusicTimer(duration: Int) {
        timerJob = CoroutineScope(context = Dispatchers.Default).launch {
            musicTimer.collect(collector = { pos: Int ->
                if (pos == duration) {
                    destroyAllProcess()
                }
            })
        }
    }

    private fun destroyAllProcess() {
        mediaPlayer?.let { player: MediaPlayer ->
            player.release()
        }
        trackingController.value = false
        timerJob?.cancel()
        stopSelf()
    }

    private fun Int.firstTwoDigits(): Int {
        return (this.toString().substring(startIndex = 0, endIndex = 2).toInt() + 1)
    }

}