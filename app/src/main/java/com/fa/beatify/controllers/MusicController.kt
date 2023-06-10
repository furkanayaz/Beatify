package com.fa.beatify.controllers

import kotlinx.coroutines.flow.MutableStateFlow

object MusicController {
    val playingController: MutableStateFlow<Boolean> = MutableStateFlow(value = false)
}