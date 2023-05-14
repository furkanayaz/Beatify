package com.fa.beatify.interfaces

import com.fa.beatify.models.Album
import com.fa.beatify.models.Artist
import com.fa.beatify.models.Genre
import com.fa.beatify.models.Track
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DeezerDao {

    @GET("genre")
    fun getGenre(): Call<Genre>

    @GET("genre/{genre_id}/artists")
    fun getArtists( @Path("genre_id") genreId: Int ): Call<Artist>

    @GET("artist/{artist_id}/albums")
    fun getAlbums( @Path("artist_id") artistId: Int ): Call<Album>

    @GET("album/{album_id}/tracks")
    fun getTracks( @Path("album_id") albumId: Int ): Call<Track>

}