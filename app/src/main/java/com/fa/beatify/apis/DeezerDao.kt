package com.fa.beatify.apis

import com.fa.beatify.models.Album
import com.fa.beatify.models.Artist
import com.fa.beatify.models.Genre
import com.fa.beatify.models.Track
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DeezerDao {
    @GET("genre")
    suspend fun getGenre(): Response<Genre>

    @GET("genre/{genre_id}/artists")
    suspend fun getArtists(@Path("genre_id") genreId: Int): Response<Artist>

    @GET("artist/{artist_id}/albums")
    suspend fun getAlbums(@Path("artist_id") artistId: Int): Response<Album>

    @GET("album/{album_id}/tracks")
    suspend fun getTracks(@Path("album_id") albumId: Int): Response<Track>
}