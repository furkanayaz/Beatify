package com.fa.beatify.utils

class DurationRepo {
    fun getDuration(durationInSeconds: Int): String {
        val minutes = durationInSeconds / 60
        val seconds = durationInSeconds - (minutes * 60)

        return "$minutes:${String.format("%02d", seconds)}"
    }
}