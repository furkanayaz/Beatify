package com.fa.beatify.domain.remote.use_cases

import com.fa.beatify.domain.mappers.toGenre
import com.fa.beatify.data.remote.services.DeezerDataSource
import com.fa.beatify.data.models.GenreDtoModel
import com.fa.beatify.domain.models.Genre

class AllGenresUseCase(
    private val deezerDataSource: DeezerDataSource
) {

    suspend operator fun invoke(): List<Genre> = deezerDataSource.getGenre()
        .body()?.data!!.map { dtoModel: GenreDtoModel -> dtoModel.toGenre() }

}