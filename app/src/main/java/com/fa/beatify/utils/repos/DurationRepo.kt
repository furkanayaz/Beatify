package com.fa.beatify.utils.repos

class DurationRepo {
    fun getDuration(durationInSeconds: Int): String {
        val minutes: Int = durationInSeconds / 60
        val seconds: Int = durationInSeconds - (minutes * 60)

        return "$minutes:${String.format("%02d", seconds)}"
    }
}