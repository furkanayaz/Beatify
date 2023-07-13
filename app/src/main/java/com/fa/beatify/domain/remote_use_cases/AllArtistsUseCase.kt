package com.fa.beatify.domain.remote_use_cases

import com.fa.beatify.domain.mappers.toArtist
import com.fa.beatify.data.remote_source.DeezerDao
import com.fa.beatify.data.models.ArtistDtoModel
import com.fa.beatify.domain.models.Artist
import javax.inject.Inject

class AllArtistsUseCase @Inject constructor(
    private val deezerDao: DeezerDao
) {

    suspend operator fun invoke(genreId: Int): List<Artist> =
        deezerDao.getArtists(genreId = genreId)
            .body()?.data!!.map { dtoModel: ArtistDtoModel -> dtoModel.toArtist() }

}