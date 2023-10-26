package com.fa.beatify.data.remote.services

import com.fa.beatify.data.models.AlbumDto
import com.fa.beatify.data.models.ArtistDto
import com.fa.beatify.data.models.GenreDto
import com.fa.beatify.data.models.TrackDto
import com.fa.beatify.data.response.Response

interface DeezerDataSource {
    suspend fun getGenre(): Response<GenreDto>

    suspend fun getArtists(genreId: Int): Response<ArtistDto>

    suspend fun getAlbums(artistId: Int): Response<AlbumDto>

    suspend fun getTracks(albumId: Int): Response<TrackDto>
}