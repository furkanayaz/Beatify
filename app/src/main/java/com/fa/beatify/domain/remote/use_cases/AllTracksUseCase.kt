package com.fa.beatify.domain.remote.use_cases

import com.fa.beatify.domain.mappers.toTrack
import com.fa.beatify.data.models.TrackDtoModel
import com.fa.beatify.domain.models.Track
import com.fa.beatify.domain.remote.impl.DeezerDataSourceImpl

class AllTracksUseCase(
    private val deezerDataSourceImpl: DeezerDataSourceImpl
) {

    suspend operator fun invoke(albumId: Int): List<Track> =
        deezerDataSourceImpl.getTracks(albumId = albumId).data!!.data!!.map { dtoModel: TrackDtoModel -> dtoModel.toTrack() }

}