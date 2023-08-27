package com.fa.beatify.utils.repos

import com.fa.beatify.data.models.Like
import com.fa.beatify.domain.models.Album
import com.fa.beatify.domain.models.Artist
import com.fa.beatify.domain.models.Genre
import com.fa.beatify.domain.models.Track

class SearchRepo<out T>(val model: T, val searchedText: String) {

    fun search(): Boolean =
        when(model) {
            is Like -> model.musicName execute searchedText
            is Genre -> model.name execute searchedText
            is Track -> model.title execute searchedText
            is Artist -> model.name execute searchedText
            is Album -> model.title execute searchedText
            else -> false
        }

    private infix fun String.execute(other: String): Boolean =
        this.lowercase().contains(other.lowercase())

}