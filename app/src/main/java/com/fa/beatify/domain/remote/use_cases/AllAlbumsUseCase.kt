package com.fa.beatify.domain.remote.use_cases

import com.fa.beatify.domain.mappers.toAlbum
import com.fa.beatify.data.models.AlbumDtoModel
import com.fa.beatify.domain.models.Album
import com.fa.beatify.domain.remote.impl.DeezerDataSourceImpl

class AllAlbumsUseCase(
    private val deezerDataSourceImpl: DeezerDataSourceImpl
) {

    suspend operator fun invoke(artistId: Int): List<Album> =
        deezerDataSourceImpl.getAlbums(artistId = artistId).data!!.data!!.map { dtoModel: AlbumDtoModel -> dtoModel.toAlbum() }

}