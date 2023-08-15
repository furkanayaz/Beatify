package com.fa.beatify.domain.remote.services

import com.fa.beatify.data.models.AlbumDto
import com.fa.beatify.data.models.ArtistDto
import com.fa.beatify.data.models.GenreDto
import com.fa.beatify.data.models.TrackDto
import com.fa.beatify.data.response.BeatifyResponse

interface DeezerResponse {
    suspend fun getGenre(): BeatifyResponse<GenreDto>
    suspend fun getArtists(genreId: Int): BeatifyResponse<ArtistDto>
    suspend fun getAlbums(artistId: Int): BeatifyResponse<AlbumDto>
    suspend fun getTracks(albumId: Int): BeatifyResponse<TrackDto>
}