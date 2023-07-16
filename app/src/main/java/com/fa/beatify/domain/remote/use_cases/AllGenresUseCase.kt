package com.fa.beatify.domain.remote.use_cases

import com.fa.beatify.domain.mappers.toGenre
import com.fa.beatify.data.remote.services.DeezerDao
import com.fa.beatify.data.models.GenreDtoModel
import com.fa.beatify.domain.models.Genre

class AllGenresUseCase(
    private val deezerDao: DeezerDao
) {

    suspend operator fun invoke(): List<Genre> = deezerDao.getGenre()
        .body()?.data!!.map { dtoModel: GenreDtoModel -> dtoModel.toGenre() }

}