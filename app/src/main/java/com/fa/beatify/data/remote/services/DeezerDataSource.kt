package com.fa.beatify.data.remote.services

import com.fa.beatify.data.models.AlbumDto
import com.fa.beatify.data.models.ArtistDto
import com.fa.beatify.data.models.GenreDto
import com.fa.beatify.data.models.TrackDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import com.fa.beatify.data.remote.Endpoints.GET_GENRE
import com.fa.beatify.data.remote.Endpoints.GET_ARTISTS
import com.fa.beatify.data.remote.Endpoints.GET_ALBUMS
import com.fa.beatify.data.remote.Endpoints.GET_TRACKS

interface DeezerDataSource {
    @GET(GET_GENRE)
    suspend fun getGenre(): Response<GenreDto>

    @GET(GET_ARTISTS)
    suspend fun getArtists(@Path("genre_id") genreId: Int): Response<ArtistDto>

    @GET(GET_ALBUMS)
    suspend fun getAlbums(@Path("artist_id") artistId: Int): Response<AlbumDto>

    @GET(GET_TRACKS)
    suspend fun getTracks(@Path("album_id") albumId: Int): Response<TrackDto>
}