package com.fa.beatify.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackDto(
    @SerialName("data") var data: ArrayList<TrackDtoModel>? = null,
    @SerialName("total") var total: Int? = null
)

@Serializable
data class TrackDtoModel(
    @SerialName("id") var id: Int? = null,
    @SerialName("readable") var readable: Boolean? = null,
    @SerialName("title") var title: String? = null,
    @SerialName("title_short") var titleShort: String? = null,
    @SerialName("title_version") var titleVersion: String? = null,
    @SerialName("isrc") var isrc: String? = null,
    @SerialName("link") var link: String? = null,
    @SerialName("duration") var duration: Int? = null,
    @SerialName("track_position") var trackPosition: Int? = null,
    @SerialName("disk_number") var diskNumber: Int? = null,
    @SerialName("rank") var rank: Int? = null,
    @SerialName("explicit_lyrics") var explicitLyrics: Boolean? = null,
    @SerialName("explicit_content_lyrics") var explicitContentLyrics: Int? = null,
    @SerialName("explicit_content_cover") var explicitContentCover: Int? = null,
    @SerialName("preview") var preview: String? = null,
    @SerialName("md5_image") var md5Image: String? = null,
    @SerialName("artist") var artistDto: ArtistDto? = ArtistDto(),
    @SerialName("type") var type: String? = null
)