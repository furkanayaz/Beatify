package com.fa.beatify.domain.remote.impl

import com.fa.beatify.data.models.AlbumDto
import com.fa.beatify.data.models.ArtistDto
import com.fa.beatify.data.models.GenreDto
import com.fa.beatify.data.models.TrackDto
import com.fa.beatify.data.remote.services.DeezerDataSource
import com.fa.beatify.data.response.BeatifyResponse
import com.fa.beatify.domain.remote.services.DeezerResponse
import retrofit2.Response

class DeezerDataImpl(
    private val deezerDataSource: DeezerDataSource
) : DeezerResponse {
    override suspend fun getGenre(): BeatifyResponse<GenreDto> {
        val genreDto: Response<GenreDto> = deezerDataSource.getGenre()

        return if (genreDto.isSuccessful) BeatifyResponse.Success(
            data = genreDto.body(), code = 200
        )
        else BeatifyResponse.Failure(code = 404)
    }

    override suspend fun getArtists(genreId: Int): BeatifyResponse<ArtistDto> {
        val artistDto: Response<ArtistDto> = deezerDataSource.getArtists(genreId = genreId)

        return if (artistDto.isSuccessful) BeatifyResponse.Success(
            data = artistDto.body(), code = 200
        )
        else BeatifyResponse.Failure(code = 404)
    }

    override suspend fun getAlbums(artistId: Int): BeatifyResponse<AlbumDto> {
        val albumDto: Response<AlbumDto> = deezerDataSource.getAlbums(artistId = artistId)

        return if (albumDto.isSuccessful) BeatifyResponse.Success(
            data = albumDto.body(), code = 200
        )
        else BeatifyResponse.Failure(code = 404)
    }

    override suspend fun getTracks(albumId: Int): BeatifyResponse<TrackDto> {
        val trackDto: Response<TrackDto> = deezerDataSource.getTracks(albumId = albumId)

        return if (trackDto.isSuccessful) BeatifyResponse.Success(
            data = trackDto.body(), code = 200
        )
        else BeatifyResponse.Failure(code = 404)
    }
}