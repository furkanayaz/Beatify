package com.fa.beatify.domain.remote.impl

import com.fa.beatify.data.models.AlbumDto
import com.fa.beatify.data.models.ArtistDto
import com.fa.beatify.data.models.GenreDto
import com.fa.beatify.data.models.TrackDto
import com.fa.beatify.data.remote.services.DeezerDataSource
import com.fa.beatify.data.response.Response
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url
import com.fa.beatify.data.remote.Endpoints.GET_GENRE
import com.fa.beatify.data.remote.Endpoints.GET_ARTISTS
import com.fa.beatify.data.remote.Endpoints.GET_ALBUMS
import com.fa.beatify.data.remote.Endpoints.GET_TRACKS
import com.fa.beatify.data.remote.HttpConst.OK
import com.fa.beatify.data.remote.HttpConst.NOT_FOUND

class DeezerDataSourceImpl(
    private val client: HttpClient
) : DeezerDataSource {
    override suspend fun getGenre(): Response<GenreDto> {
        return try {
            Response.Success(data = client.get {
                url(GET_GENRE)
            }.body<GenreDto>(), code = OK)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(code = NOT_FOUND)
        }
    }

    override suspend fun getArtists(genreId: Int): Response<ArtistDto> {
        return try {
            Response.Success(data = client.get {
                url(GET_ARTISTS.replace(oldValue = "*", "$genreId"))
            }.body<ArtistDto>(), code = OK)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(code = NOT_FOUND)
        }
    }

    override suspend fun getAlbums(artistId: Int): Response<AlbumDto> {
        return try {
            Response.Success(data = client.get {
                url(GET_ALBUMS.replace(oldValue = "*", newValue = "$artistId"))
            }.body<AlbumDto>(), code = OK)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(code = NOT_FOUND)
        }
    }

    override suspend fun getTracks(albumId: Int): Response<TrackDto> {
        return try {
            Response.Success(data = client.get {
                url(GET_TRACKS.replace(oldValue = "*", newValue = "$albumId"))
            }.body<TrackDto>(), code = OK)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(code = NOT_FOUND)
        }
    }
}