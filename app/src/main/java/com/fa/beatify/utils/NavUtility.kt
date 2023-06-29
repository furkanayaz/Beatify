package com.fa.beatify.utils

sealed class NavUtility(var route: String) {
    // It's not used.
    /*object Categories: NavUtility(route = "musiccategories")
    object Likes: NavUtility(route = "likes")*/
    object Artists: NavUtility(route = "artists")
    object ArtistDetail: NavUtility(route = "artist_detail")
    object AlbumDetail: NavUtility(route = "album_detail")

    fun withRouteArgs(vararg arguments: String) = buildString {
        append(route)
        arguments.forEach { arg: String -> append("/{$arg}") }
    }

    fun withSourceArgs(vararg arguments: String): String = buildString {
        append(route)
        arguments.forEach { arg: String -> append("/$arg") }
    }
}