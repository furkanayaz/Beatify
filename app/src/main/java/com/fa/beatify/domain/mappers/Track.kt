package com.fa.beatify.domain.mappers

import com.fa.beatify.data.models.TrackDtoModel
import com.fa.beatify.domain.models.Track

fun TrackDtoModel.toTrack(): Track = Track(
    id = id ?: 0,
    title = title ?: "",
    md5Image = md5Image ?: "",
    duration = duration ?: 0,
    preview = preview ?: ""
)