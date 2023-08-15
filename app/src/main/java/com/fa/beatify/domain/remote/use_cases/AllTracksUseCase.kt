package com.fa.beatify.domain.remote.use_cases

import com.fa.beatify.domain.mappers.toTrack
import com.fa.beatify.data.models.TrackDtoModel
import com.fa.beatify.domain.models.Track
import com.fa.beatify.domain.remote.impl.DeezerDataImpl

class AllTracksUseCase(
    private val deezerDataImpl: DeezerDataImpl
) {

    suspend operator fun invoke(albumId: Int): List<Track> =
        deezerDataImpl.getTracks(albumId = albumId).data!!.data!!.map { dtoModel: TrackDtoModel -> dtoModel.toTrack() }

}