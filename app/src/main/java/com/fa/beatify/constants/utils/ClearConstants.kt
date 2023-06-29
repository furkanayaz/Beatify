package com.fa.beatify.constants.utils

import com.fa.beatify.constants.controller.ListState

object ClearConstants {

    fun clearListConsts() {
        ListState.apply {
            CATEGORIES_STATE = 0
            LIKES_STATE = 0
            ARTISTS_STATE = 0
            ARTIST_DETAIL_STATE = 0
        }
    }

    fun clearImageConsts() {
        ImageConstants.apply {
            ARTIST_IMAGE = null
        }
    }

}