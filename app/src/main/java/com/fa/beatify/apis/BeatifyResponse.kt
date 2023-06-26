package com.fa.beatify.apis

sealed class BeatifyResponse<T>(
    val data: T? = null, val code: Int? = null
) {
    class Success<T>(data: T? = null, code: Int) : BeatifyResponse<T>(data = data, code = code)
    class Failure<T>(code: Int) : BeatifyResponse<T>(code = code)
    class Loading<T> : BeatifyResponse<T>()
}