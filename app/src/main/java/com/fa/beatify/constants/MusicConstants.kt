package com.fa.beatify.constants

import android.media.MediaPlayer
import com.fa.beatify.entities.LikeEntities
import com.fa.beatify.models.PlayMusic
import com.fa.beatify.models.TrackModel
import kotlinx.coroutines.flow.MutableStateFlow

object MusicConstants {
    val trackingController: MutableStateFlow<Boolean> = MutableStateFlow(value = false)
    val playingController: MutableStateFlow<Boolean> = MutableStateFlow(value = false)
    var likeList: List<LikeEntities>? = null
    var trackList: List<TrackModel>? = null
    var playMusic: PlayMusic? = null
    var mediaPlayer: MediaPlayer? = null
}