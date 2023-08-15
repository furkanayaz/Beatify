package com.fa.beatify.data.response

sealed class BeatifyResponse<T>(
    val data: T? = null, val code: Int? = null
) {
    class Success<T>(data: T? = null, code: Int) : BeatifyResponse<T>(data = data, code = code)
    class Failure<T>(code: Int) : BeatifyResponse<T>(code = code)
    class Loading<T> : BeatifyResponse<T>()

    class Idle<T> : BeatifyResponse<T>()
}