package com.fa.beatify.domain.remote.use_cases

import com.fa.beatify.domain.mappers.toTrack
import com.fa.beatify.data.remote.services.DeezerDataSource
import com.fa.beatify.data.models.TrackDtoModel
import com.fa.beatify.domain.models.Track

class AllTracksUseCase(
    private val deezerDataSource: DeezerDataSource
) {

    suspend operator fun invoke(albumId: Int): List<Track> = deezerDataSource.getTracks(albumId = albumId)
        .body()?.data!!.map { dtoModel: TrackDtoModel -> dtoModel.toTrack() }

}