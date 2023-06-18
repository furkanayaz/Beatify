package com.fa.beatify.pages.artists

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fa.beatify.models.Artist
import com.fa.beatify.models.ArtistModel
import com.fa.beatify.builders.RetrofitBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArtistsRepo {
    private val artists = MutableLiveData<List<ArtistModel>>()

    fun allArtists(genreId: Int) {
        CoroutineScope(context = Dispatchers.Main).launch {
            RetrofitBuilder.getDao().getArtists(genreId = genreId).enqueue(object : Callback<Artist> {
                override fun onResponse(call: Call<Artist>, response: Response<Artist>) {
                    if (response.isSuccessful) {
                        artists.postValue(response.body()?.data)
                    }else {
                        Log.e("Artists Response", "Unsuccessful")
                    }
                }

                override fun onFailure(call: Call<Artist>, t: Throwable) {
                    Log.e("Artists Response", t.message!!)
                }
            })
        }
    }

    fun getArtists(): MutableLiveData<List<ArtistModel>> = artists
}