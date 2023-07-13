package com.fa.beatify.domain.mappers

import com.fa.beatify.data.models.AlbumDtoModel
import com.fa.beatify.domain.models.Album

fun AlbumDtoModel.toAlbum(): Album = Album(
    id = id ?: 0,
    title = title ?: "",
    cover = cover ?: "",
    releaseDate = releaseDate ?: ""
)