package com.fa.beatify.domain.remote.use_cases

import com.fa.beatify.domain.mappers.toArtist
import com.fa.beatify.data.remote.services.DeezerDataSource
import com.fa.beatify.data.models.ArtistDtoModel
import com.fa.beatify.domain.models.Artist

class AllArtistsUseCase(
    private val deezerDataSource: DeezerDataSource
) {

    suspend operator fun invoke(genreId: Int): List<Artist> =
        deezerDataSource.getArtists(genreId = genreId)
            .body()?.data!!.map { dtoModel: ArtistDtoModel -> dtoModel.toArtist() }

}