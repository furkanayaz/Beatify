package com.fa.beatify.data.response

sealed class Response<out T>(
    val data: T? = null, val code: Int? = null
) {
    class Success<out T>(data: T? = null, code: Int) : Response<T>(data = data, code = code)
    class Failure<out T>(code: Int) : Response<T>(code = code)
    object Loading : Response<Nothing>()
}