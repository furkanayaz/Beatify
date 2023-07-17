package com.fa.beatify.data.remote.di

import com.fa.beatify.data.remote.services.DeezerDao
import com.fa.beatify.utils.constants.ApiConstants
import okhttp3.OkHttpClient
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val dataRemoteModule: Module = module {

    single {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder().baseUrl(ApiConstants.BASE_URL).client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(DeezerDao::class.java)
    }

}