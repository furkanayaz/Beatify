package com.fa.beatify.domain.remote.use_cases

import com.fa.beatify.domain.mappers.toGenre
import com.fa.beatify.data.models.GenreDtoModel
import com.fa.beatify.domain.models.Genre
import com.fa.beatify.domain.remote.impl.DeezerDataSourceImpl

class AllGenresUseCase(
    private val deezerDataSourceImpl: DeezerDataSourceImpl
) {

    suspend operator fun invoke(): List<Genre> =
        deezerDataSourceImpl.getGenre().data!!.data!!.map { dtoModel: GenreDtoModel -> dtoModel.toGenre() }

}