package com.fa.beatify.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("data" )
    @Expose
    var data : ArrayList<GenreModel>? = null
)