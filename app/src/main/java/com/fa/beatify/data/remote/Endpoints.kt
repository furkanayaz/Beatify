package com.fa.beatify.data.remote

object Endpoints {
    private const val BASE_URL = "https://api.deezer.com/"

    const val GET_GENRE = "${BASE_URL}genre"
    const val GET_ARTISTS = "${BASE_URL}genre/*/artists"
    const val GET_ALBUMS = "${BASE_URL}artist/*/albums"
    const val GET_TRACKS = "${BASE_URL}album/*/tracks"
}