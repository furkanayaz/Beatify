package com.fa.beatify.utils.network

import kotlinx.coroutines.flow.Flow


interface Connection {

    fun observe(): Flow<Status>

    enum class Status {
        Available, Unavailable, Losing, Lost
    }

}