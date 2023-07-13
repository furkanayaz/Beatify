package com.fa.beatify.domain.remote_use_cases

import com.fa.beatify.domain.mappers.toAlbum
import com.fa.beatify.data.remote_source.DeezerDao
import com.fa.beatify.data.models.AlbumDtoModel
import com.fa.beatify.domain.models.Album
import javax.inject.Inject

class AllAlbumsUseCase @Inject constructor(
    private val deezerDao: DeezerDao
) {

    suspend operator fun invoke(artistId: Int): List<Album> =
        deezerDao.getAlbums(artistId = artistId)
            .body()?.data!!.map { dtoModel: AlbumDtoModel -> dtoModel.toAlbum() }

}