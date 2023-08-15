package com.fa.beatify.domain.remote.use_cases

import com.fa.beatify.domain.mappers.toAlbum
import com.fa.beatify.data.models.AlbumDtoModel
import com.fa.beatify.domain.models.Album
import com.fa.beatify.domain.remote.impl.DeezerDataImpl

class AllAlbumsUseCase(
    private val deezerDataImpl: DeezerDataImpl
) {

    suspend operator fun invoke(artistId: Int): List<Album> =
        deezerDataImpl.getAlbums(artistId = artistId).data!!.data!!.map { dtoModel: AlbumDtoModel -> dtoModel.toAlbum() }

}