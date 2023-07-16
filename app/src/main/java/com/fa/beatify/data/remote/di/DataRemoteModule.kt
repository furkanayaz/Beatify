package com.fa.beatify.data.remote.di

import com.fa.beatify.data.remote.services.DeezerDao
import com.fa.beatify.utils.constants.ApiConstants
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataRemoteModule: Module = module {

    single {
        Retrofit.Builder().baseUrl(ApiConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build().create(DeezerDao::class.java)
    }

}