package com.fa.beatify.apis

import com.fa.beatify.entities.LikeEntities
import com.fa.beatify.models.Album
import com.fa.beatify.models.Artist
import com.fa.beatify.models.Genre
import com.fa.beatify.models.Track
import javax.inject.Inject

class BeatifyRepo @Inject constructor(
    private val deezerDao: DeezerDao, private val likeDao: LikeDao
) {
    // Retrofit Api Services
    suspend fun allGenres(): Genre? = deezerDao.getGenre().body()
    suspend fun allArtists(genreId: Int): Artist? = deezerDao.getArtists(genreId = genreId).body()
    suspend fun allAlbums(artistId: Int): Album? = deezerDao.getAlbums(artistId = artistId).body()
    suspend fun allTracks(albumId: Int): Track? = deezerDao.getTracks(albumId = albumId).body()

    // Room Api Services
    suspend fun allLikes(): List<LikeEntities> = likeDao.getLikes()
    suspend fun insertLike(like: LikeEntities): Unit = likeDao.insertLike(like = like)
    suspend fun deleteLike(like: LikeEntities): Unit = likeDao.deleteLike(like = like)
}