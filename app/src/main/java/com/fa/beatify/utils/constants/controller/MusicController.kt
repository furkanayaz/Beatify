package com.fa.beatify.utils.constants.controller

import android.media.MediaPlayer
import com.fa.beatify.data.models.Like
import com.fa.beatify.domain.models.PlayMusic
import com.fa.beatify.domain.models.Track
import kotlinx.coroutines.flow.MutableStateFlow

object MusicController {
    val trackingController: MutableStateFlow<Boolean> = MutableStateFlow(value = false)
    val playingController: MutableStateFlow<Boolean> = MutableStateFlow(value = false)
    var likeList: List<Like> = emptyList()
    var trackList: List<Track> = emptyList()
    var playMusic: PlayMusic? = null
    var mediaPlayer: MediaPlayer? = null
}