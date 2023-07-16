package com.fa.beatify.data.remote.services

import com.fa.beatify.data.models.AlbumDto
import com.fa.beatify.data.models.ArtistDto
import com.fa.beatify.data.models.GenreDto
import com.fa.beatify.data.models.TrackDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DeezerDao {
    @GET("genre")
    suspend fun getGenre(): Response<GenreDto>

    @GET("genre/{genre_id}/artists")
    suspend fun getArtists(@Path("genre_id") genreId: Int): Response<ArtistDto>

    @GET("artist/{artist_id}/albums")
    suspend fun getAlbums(@Path("artist_id") artistId: Int): Response<AlbumDto>

    @GET("album/{album_id}/tracks")
    suspend fun getTracks(@Path("album_id") albumId: Int): Response<TrackDto>
}