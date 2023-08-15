package com.fa.beatify.domain.remote.use_cases

import com.fa.beatify.domain.mappers.toGenre
import com.fa.beatify.data.models.GenreDtoModel
import com.fa.beatify.domain.models.Genre
import com.fa.beatify.domain.remote.impl.DeezerDataImpl

class AllGenresUseCase(
    private val deezerDataImpl: DeezerDataImpl
) {

    suspend operator fun invoke(): List<Genre> =
        deezerDataImpl.getGenre().data!!.data!!.map { dtoModel: GenreDtoModel -> dtoModel.toGenre() }

}