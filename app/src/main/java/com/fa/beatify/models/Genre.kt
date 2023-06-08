package com.fa.beatify.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("data" )
    @Expose
    var data : ArrayList<GenreModel>? = null
)

data class GenreModel(
    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("name")
    @Expose
    var name: String? = null,
    @SerializedName("picture")
    @Expose
    var picture: String? = null,
    @SerializedName("picture_small")
    @Expose
    var pictureSmall: String? = null,
    @SerializedName("picture_medium")
    @Expose
    var pictureMedium: String? = null,
    @SerializedName("picture_big")
    @Expose
    var pictureBig: String? = null,
    @SerializedName("picture_xl")
    @Expose
    var pictureXl: String? = null,
    @SerializedName("type")
    @Expose
    var type: String? = null
)