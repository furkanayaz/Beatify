package com.fa.beatify.pages.album_detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fa.beatify.entities.LikeEntities
import com.fa.beatify.models.Track
import com.fa.beatify.models.TrackModel
import com.fa.beatify.builders.RetrofitBuilder
import com.fa.beatify.builders.RoomDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlbumDetailRepo {
    private val trackList = MutableLiveData<List<TrackModel>>()

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

    fun getImage(md5Image: String): String = "https://e-cdns-images.dzcdn.net/images/cover/$md5Image/500x500-000000-80-0-0.jpg"

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

    fun getTrackList(): MutableLiveData<List<TrackModel>> = trackList

}