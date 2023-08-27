package com.fa.beatify.utils.constants.controller

import com.fa.beatify.utils.constants.ListState

object ClearController {

    fun clearListConsts() {
        ListState.apply {
            CATEGORIES_STATE = 0
            LIKES_STATE = 0
            ARTISTS_STATE = 0
            ARTIST_DETAIL_STATE = 0
        }
    }

    fun clearImageConsts() {
        ImageController.apply {
            ARTIST_IMAGE = null
        }
    }

}