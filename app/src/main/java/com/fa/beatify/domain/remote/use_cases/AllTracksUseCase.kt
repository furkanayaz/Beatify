package com.fa.beatify.domain.remote.use_cases

import com.fa.beatify.domain.mappers.toTrack
import com.fa.beatify.data.remote.services.DeezerDao
import com.fa.beatify.data.models.TrackDtoModel
import com.fa.beatify.domain.models.Track

class AllTracksUseCase(
    private val deezerDao: DeezerDao
) {

    suspend operator fun invoke(albumId: Int): List<Track> = deezerDao.getTracks(albumId = albumId)
        .body()?.data!!.map { dtoModel: TrackDtoModel -> dtoModel.toTrack() }

}