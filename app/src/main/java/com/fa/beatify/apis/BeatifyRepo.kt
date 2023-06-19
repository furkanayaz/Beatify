package com.fa.beatify.apis

import com.fa.beatify.entities.LikeEntities
import com.fa.beatify.models.AlbumModel
import com.fa.beatify.models.ArtistModel
import com.fa.beatify.models.GenreModel
import com.fa.beatify.models.TrackModel
import javax.inject.Inject

class BeatifyRepo @Inject constructor(
    private val deezerDao: DeezerDao, private val likeDao: LikeDao
) {
    // Retrofit Api Services
    suspend fun allGenres(): ArrayList<GenreModel>? = deezerDao.getGenre().body()?.data
    suspend fun allArtists(genreId: Int): ArrayList<ArtistModel>? = deezerDao.getArtists(genreId = genreId).body()?.data
    suspend fun allAlbums(artistId: Int): ArrayList<AlbumModel>? = deezerDao.getAlbums(artistId = artistId).body()?.data
    suspend fun allTracks(albumId: Int): ArrayList<TrackModel>? = deezerDao.getTracks(albumId = albumId).body()?.data

    // Room Api Services
    suspend fun allLikes(): List<LikeEntities> = likeDao.getLikes()
    suspend fun insertLike(like: LikeEntities): Unit = likeDao.insertLike(like = like)
    suspend fun deleteLike(like: LikeEntities): Unit = likeDao.deleteLike(like = like)
}