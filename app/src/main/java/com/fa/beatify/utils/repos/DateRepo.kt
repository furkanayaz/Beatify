package com.fa.beatify.utils.repos

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar

class DateRepo {

    @SuppressLint("SimpleDateFormat")
    fun getReleaseDate(releaseDate: String): String {
        val tempCalendar = Calendar.getInstance()
        val tempSdf = SimpleDateFormat("yyyy-dd-MM")
        tempCalendar.time = tempSdf.parse(releaseDate)!!
        val sdf = SimpleDateFormat("dd MMM yyyy")

        return sdf.format(tempCalendar.time)
    }

}