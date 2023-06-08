package com.fa.beatify.services

import android.app.Service
import android.content.Intent
import android.os.IBinder

class PlayerService: Service() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        /* Sistem servisi herhangi bir nedenden dolayı durdurursa,
           tekrardan müziğin çalışmamasını istediğim için (mantıklı olarak) NOT_STICKY yaptım. */
        return START_NOT_STICKY
    }

}