package com.fa.beatify.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fa.beatify.entities.LikeEntities
import com.fa.beatify.models.TrackModel
import com.fa.beatify.repositories.AlbumDetailRepo

class AlbumDetailVM: ViewModel() {
    private val albumDetailRepo = AlbumDetailRepo()

    fun getTracks(albumId: Int) = albumDetailRepo.getTracks(albumId = albumId)

    fun getDuration(durationInSeconds: Int): String = albumDetailRepo.getDuration(durationInSeconds = durationInSeconds)

    fun insertLike(like: LikeEntities) = albumDetailRepo.insertLike(like = like)

    fun deleteLike(like: LikeEntities) = albumDetailRepo.deleteLike(like = like)

    fun playMusic(url: String) = albumDetailRepo.playMusic(url = url)

    fun stopMusic() = albumDetailRepo.stopMusic()

    fun getTrackList(): MutableLiveData<List<TrackModel>> = albumDetailRepo.getTrackList()
}