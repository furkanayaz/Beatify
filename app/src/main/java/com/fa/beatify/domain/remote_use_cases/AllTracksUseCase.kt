package com.fa.beatify.domain.remote_use_cases

import com.fa.beatify.domain.mappers.toTrack
import com.fa.beatify.data.remote_source.DeezerDao
import com.fa.beatify.data.models.TrackDtoModel
import com.fa.beatify.domain.models.Track
import javax.inject.Inject

class AllTracksUseCase @Inject constructor(
    private val deezerDao: DeezerDao
) {

    suspend operator fun invoke(albumId: Int): List<Track> = deezerDao.getTracks(albumId = albumId)
        .body()?.data!!.map { dtoModel: TrackDtoModel -> dtoModel.toTrack() }

}