package com.fa.beatify.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistDto(
    @SerialName("data") var data: ArrayList<ArtistDtoModel>? = null
)

@Serializable
data class ArtistDtoModel(
    @SerialName("id") var id: Int? = null,
    @SerialName("name") var name: String? = null,
    @SerialName("picture") var picture: String? = null,
    @SerialName("picture_small") var pictureSmall: String? = null,
    @SerialName("picture_medium") var pictureMedium: String? = null,
    @SerialName("picture_big") var pictureBig: String? = null,
    @SerialName("picture_xl") var pictureXl: String? = null,
    @SerialName("radio") var radio: Boolean? = null,
    @SerialName("tracklist") var tracklist: String? = null,
    @SerialName("type") var type: String? = null
)