package com.fa.beatify

import android.app.Application
import com.fa.beatify.common.di.allModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BeatifyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BeatifyApp)
            modules(
                allModules
            )
        }
    }

}