package com.fa.beatify.pages.artist_detail

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fa.beatify.models.Album
import com.fa.beatify.models.AlbumModel
import com.fa.beatify.builders.RetrofitBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar

class ArtistDetailRepo {
    private val albumModel = MutableLiveData<List<AlbumModel>>()

    fun getAlbums(artistId: Int) {
        CoroutineScope(context = Dispatchers.Main).launch {
            RetrofitBuilder.getDao().getAlbums(artistId = artistId).enqueue(object : Callback<Album> {
                override fun onResponse(call: Call<Album>, response: Response<Album>) {
                    if (response.isSuccessful)
                        albumModel.postValue(response.body()?.data)
                    else
                        Log.e("Album Status", "Unsuccessful")

                    cancel()
                }

                override fun onFailure(call: Call<Album>, t: Throwable) {
                    Log.e("Album Status", "Failure")

                    cancel()
                }
            })
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getReleaseDate(releaseDate: String): String {
        val tempCalendar = Calendar.getInstance()
        val tempSdf = SimpleDateFormat("yyyy-dd-MM")
        tempCalendar.time = tempSdf.parse(releaseDate)!!
        val sdf = SimpleDateFormat("dd MMM yyyy")

        return sdf.format(tempCalendar.time)
    }

    fun getAlbumModel(): MutableLiveData<List<AlbumModel>> = albumModel

}