package com.fa.beatify.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlbumDto(
    @SerialName("data") var data: ArrayList<AlbumDtoModel>? = null,
    @SerialName("total") var total: Int? = null,
    @SerialName("next") var next: String? = null
)

@Serializable
data class AlbumDtoModel(
    @SerialName("id") var id: Int? = null,
    @SerialName("title") var title: String? = null,
    @SerialName("link") var link: String? = null,
    @SerialName("cover") var cover: String? = null,
    @SerialName("cover_small") var coverSmall: String? = null,
    @SerialName("cover_medium") var coverMedium: String? = null,
    @SerialName("cover_big") var coverBig: String? = null,
    @SerialName("cover_xl") var coverXl: String? = null,
    @SerialName("md5_image") var md5Image: String? = null,
    @SerialName("genre_id") var genreId: Int? = null,
    @SerialName("fans") var fans: Int? = null,
    @SerialName("release_date") var releaseDate: String? = null,
    @SerialName("record_type") var recordType: String? = null,
    @SerialName("tracklist") var tracklist: String? = null,
    @SerialName("explicit_lyrics") var explicitLyrics: Boolean? = null,
    @SerialName("type") var type: String? = null
)