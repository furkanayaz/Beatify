package com.fa.beatify.utils.di

import com.fa.beatify.utils.network.NetworkConnection
import com.fa.beatify.utils.repos.DateRepo
import com.fa.beatify.utils.repos.DurationRepo
import com.fa.beatify.utils.repos.ImageRepo
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

val utilsModule: Module = module {

    single {
        NetworkConnection(context = androidContext())
    }

    single {
        DateRepo()
    }

    single {
        DurationRepo()
    }

    factory {
        ImageRepo()
    }

}