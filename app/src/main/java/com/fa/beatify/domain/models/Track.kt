package com.fa.beatify.domain.models

data class Track(
    val id: Int,
    val title: String,
    val md5Image: String,
    val duration: Int,
    val preview: String
)