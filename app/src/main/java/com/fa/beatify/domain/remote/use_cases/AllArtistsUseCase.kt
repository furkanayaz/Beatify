package com.fa.beatify.domain.remote.use_cases

import com.fa.beatify.domain.mappers.toArtist
import com.fa.beatify.data.models.ArtistDtoModel
import com.fa.beatify.domain.models.Artist
import com.fa.beatify.domain.remote.impl.DeezerDataImpl

class AllArtistsUseCase(
    private val deezerDataImpl: DeezerDataImpl
) {

    suspend operator fun invoke(genreId: Int): List<Artist> =
        deezerDataImpl.getArtists(genreId = genreId).data!!.data!!.map { dtoModel: ArtistDtoModel -> dtoModel.toArtist() }

}