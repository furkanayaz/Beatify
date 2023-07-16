package com.fa.beatify

import android.app.Application
import com.fa.beatify.data.local.di.dataLocalModule
import com.fa.beatify.data.remote.di.dataRemoteModule
import com.fa.beatify.domain.local.di.domainLocalModule
import com.fa.beatify.domain.remote.di.domainRemoteModule
import com.fa.beatify.presentation.di.vmModule
import com.fa.beatify.utils.di.utilsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BeatifyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BeatifyApp)
            modules(
                dataLocalModule,
                dataRemoteModule,
                domainLocalModule,
                domainRemoteModule,
                vmModule,
                utilsModule
            )
        }
    }

}