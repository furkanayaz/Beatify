package com.fa.beatify.domain.remote_use_cases

import com.fa.beatify.domain.mappers.toGenre
import com.fa.beatify.data.remote_source.DeezerDao
import com.fa.beatify.data.models.GenreDtoModel
import com.fa.beatify.domain.models.Genre
import javax.inject.Inject

class AllGenresUseCase @Inject constructor(
    private val deezerDao: DeezerDao
) {

    suspend operator fun invoke(): List<Genre> = deezerDao.getGenre()
        .body()?.data!!.map { dtoModel: GenreDtoModel -> dtoModel.toGenre() }

}