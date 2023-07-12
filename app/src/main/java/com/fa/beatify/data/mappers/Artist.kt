package com.fa.beatify.data.mappers

import com.fa.beatify.data.models.ArtistDtoModel
import com.fa.beatify.domain.models.Artist

fun ArtistDtoModel.toArtist(): Artist = Artist(
    id = id ?: 0,
    name = name ?: "",
    pictureMedium = pictureMedium ?: "",
    picture = picture ?: ""
)