package com.fa.beatify.data.mappers

import com.fa.beatify.data.models.GenreDtoModel
import com.fa.beatify.domain.models.Genre

fun GenreDtoModel.toGenre(): Genre = Genre(id = id ?: 0, name = name ?: "", picture = picture ?: "")