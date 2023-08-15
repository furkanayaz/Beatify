package com.fa.beatify.domain.remote.use_cases

import com.fa.beatify.domain.mappers.toAlbum
import com.fa.beatify.data.remote.services.DeezerDataSource
import com.fa.beatify.data.models.AlbumDtoModel
import com.fa.beatify.domain.models.Album

class AllAlbumsUseCase(
    private val deezerDataSource: DeezerDataSource
) {

    suspend operator fun invoke(artistId: Int): List<Album> =
        deezerDataSource.getAlbums(artistId = artistId)
            .body()?.data!!.map { dtoModel: AlbumDtoModel -> dtoModel.toAlbum() }

}