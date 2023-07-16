package com.fa.beatify.domain.remote.use_cases

import com.fa.beatify.domain.mappers.toArtist
import com.fa.beatify.data.remote.services.DeezerDao
import com.fa.beatify.data.models.ArtistDtoModel
import com.fa.beatify.domain.models.Artist

class AllArtistsUseCase(
    private val deezerDao: DeezerDao
) {

    suspend operator fun invoke(genreId: Int): List<Artist> =
        deezerDao.getArtists(genreId = genreId)
            .body()?.data!!.map { dtoModel: ArtistDtoModel -> dtoModel.toArtist() }

}