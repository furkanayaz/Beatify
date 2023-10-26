package com.fa.beatify.data.remote.di

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import com.fa.beatify.data.remote.KtorConst.TIME_OUT
import com.fa.beatify.data.remote.KtorConst.LOGGER_TAG
import com.fa.beatify.data.remote.KtorConst.HTTP_STATUS_LOGGER_TAG

val dataRemoteModule: Module = module {

    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.i(LOGGER_TAG, message)
                    }

                }
            }
            install(ResponseObserver) {
                onResponse { response: HttpResponse ->
                    Log.i(HTTP_STATUS_LOGGER_TAG, response.status.value.toString())
                    Log.i(HTTP_STATUS_LOGGER_TAG, response.requestTime.timestamp.toString())
                    Log.i(HTTP_STATUS_LOGGER_TAG, response.responseTime.timestamp.toString())
                }
            }
            install(DefaultRequest) {
                header(key = HttpHeaders.ContentType, value = ContentType.Application.Json)
            }
            engine {
                connectTimeout = TIME_OUT
                socketTimeout = TIME_OUT
            }
        }
    }

}