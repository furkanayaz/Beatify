package com.fa.beatify.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fa.beatify.entities.LikeEntities
import com.fa.beatify.repositories.LikesRepo
import com.fa.beatify.repositories.MusicPlayerRepo
import kotlinx.coroutines.flow.MutableStateFlow

class LikesVM: ViewModel() {
    private val likesRepo = LikesRepo()
    private val musicPlayerRepo = MusicPlayerRepo()

    fun deleteLike(like: LikeEntities) = likesRepo.deleteLike(like = like)

    fun getLikesData(): MutableLiveData<List<LikeEntities>> = likesRepo.getLikesData()

    fun playMusic(url: String) = musicPlayerRepo.playMusic(url = url)
    fun stopMusic() = musicPlayerRepo.stopMusic()

    fun getPlayingController(): MutableStateFlow<Boolean> = musicPlayerRepo.getPlayingController()

}