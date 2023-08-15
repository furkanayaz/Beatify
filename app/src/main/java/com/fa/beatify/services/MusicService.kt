package com.fa.beatify.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.fa.beatify.presentation.activity.MainActivity
import com.fa.beatify.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import com.fa.beatify.utils.constants.utils.MusicConstants.mediaPlayer
import com.fa.beatify.utils.constants.utils.MusicConstants.trackingController
import com.fa.beatify.utils.constants.utils.MusicConstants.playMusic
import com.fa.beatify.utils.constants.NotificationConstants.CHANNEL_ID
import com.fa.beatify.utils.constants.NotificationConstants.CHANNEL_NAME
import com.fa.beatify.utils.constants.NotificationConstants.NOTIFICATION_ID
import com.fa.beatify.utils.constants.NotificationConstants.REQUEST_CODE
import com.fa.beatify.domain.models.PlayMusic

/***
 *
 * ATTENTION PLEASE!
 *
 * THIS CLASS WILL MODIFY ACCORDING TO SINGLETON DESIGN PATTERN.
 * ALSO WILL ADD A MUSIC THEME NOTIFICATION INSTEAD CLASSIC NOTIFICATION.
 *
 * */

class MusicService : Service() {
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
            try {
                playMusic(url = locUrl)
                showNotification()
            } catch (e: Exception) {
                e.printStackTrace()
            }
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

        try {
            while (true) {
                if (mediaPlayer!!.isPlaying) {
                    emit(value = currentPos)
                    currentPos++
                }

                delay(timeMillis = 1000L)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showNotification() {
        playMusic?.let { playMusic: PlayMusic ->
            val manager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val builder: NotificationCompat.Builder?
            val intent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                this,
                REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel: NotificationChannel? = manager.getNotificationChannel(CHANNEL_ID)

                if (channel == null) {
                    val tempChannel = NotificationChannel(
                        CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
                    )
                    manager.createNotificationChannel(tempChannel)
                }
            }

            builder = NotificationCompat.Builder(this, CHANNEL_ID).also {
                it.setStyle(androidx.media.app.NotificationCompat.MediaStyle())
                it.priority = NotificationCompat.PRIORITY_LOW
                it.setAutoCancel(false)
                it.setSmallIcon(R.mipmap.light_icon)
                it.setContentIntent(pendingIntent)
                it.setShowWhen(false)
                it.setOnlyAlertOnce(true)
                it.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                it.setContentTitle(playMusic.artistName)
                it.setContentText("${playMusic.albumName} - ${playMusic.musicName}")
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