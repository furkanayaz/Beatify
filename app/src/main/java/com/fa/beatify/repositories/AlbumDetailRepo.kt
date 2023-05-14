package com.fa.beatify.repositories

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fa.beatify.entities.LikeEntities
import com.fa.beatify.models.Track
import com.fa.beatify.models.TrackModel
import com.fa.beatify.retrofits.RetrofitBuilder
import com.fa.beatify.rooms.RoomDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class AlbumDetailRepo {
    private val trackList = MutableLiveData<List<TrackModel>>()
    private var mediaPlayer: MediaPlayer? = null

    init {
        mediaPlayer = MediaPlayer()
    }

    fun getTracks(albumId: Int) {
        CoroutineScope(context = Dispatchers.Main).launch {
            RetrofitBuilder.getDao().getTracks(albumId = albumId).enqueue(object : Callback<Track> {
                override fun onResponse(call: Call<Track>, response: Response<Track>) {
                    if (response.isSuccessful)
                        trackList.postValue(response.body()?.data)
                    else
                        Log.e("Album Status", "Unsuccessful")

                    cancel()

                }

                override fun onFailure(call: Call<Track>, t: Throwable) {
                    Log.e("Album Status", "Failure")

                    cancel()
                }
            })
        }
    }

    fun getDuration(durationInSeconds: Int): String {
        val minutes = durationInSeconds / 60
        val seconds = durationInSeconds - (minutes * 60)

        return "$minutes:${String.format("%02d", seconds)}"
    }

    fun insertLike(like: LikeEntities) {
        CoroutineScope(context = Dispatchers.Main).launch {
            RoomDB.INSTANCE?.getDao()?.insertLike(like = like)

            cancel()
        }
    }

    fun deleteLike(like: LikeEntities) {
        CoroutineScope(context = Dispatchers.Main).launch {
            RoomDB.INSTANCE?.getDao()?.deleteLike(like = like)

            cancel()
        }
    }

    fun playMusic(url: String) {
        mediaPlayer!!.setDataSource(url)
        mediaPlayer!!.prepare()
        mediaPlayer!!.start()
    }

    fun stopMusic() {
        mediaPlayer!!.stop()
        mediaPlayer!!.reset()
    }

    fun getTrackList(): MutableLiveData<List<TrackModel>> = trackList

}