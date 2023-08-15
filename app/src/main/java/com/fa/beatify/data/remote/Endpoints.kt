package com.fa.beatify.data.remote

object Endpoints {
    const val GET_GENRE = "genre"
    const val GET_ARTISTS = "genre/{genre_id}/artists"
    const val GET_ALBUMS = "artist/{artist_id}/albums"
    const val GET_TRACKS = "album/{album_id}/tracks"
}