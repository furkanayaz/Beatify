package com.fa.beatify.retrofits

import com.fa.beatify.interfaces.DeezerDao
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private var INSTANCE: Retrofit? = null
    private const val BASE_URL = "https://api.deezer.com/"

    private fun build(): Retrofit {

        if (INSTANCE == null) {
            INSTANCE = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return INSTANCE!!
    }

    fun getDao(): DeezerDao = build().create(DeezerDao::class.java)
}