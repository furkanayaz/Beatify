package com.fa.beatify.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fa.beatify.models.Genre
import com.fa.beatify.models.GenreModel
import com.fa.beatify.retrofits.RetrofitBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GenreRepo {
    private val genres = MutableLiveData<List<GenreModel>>()

    init {
        allGenres()
    }

    private fun allGenres() {
        CoroutineScope(context = Dispatchers.Main).launch {
            RetrofitBuilder.getDao().getGenre().enqueue(object : Callback<Genre> {
                override fun onResponse(call: Call<Genre>, response: Response<Genre>) {
                    if (response.isSuccessful)
                        genres.postValue(response.body()?.data)
                    else
                        Log.e("Genre Status", "Response failed")
                }

                override fun onFailure(call: Call<Genre>, t: Throwable) {
                    // Alert gösterilecek.
                }
            })
        }
    }

    fun getGenres(): MutableLiveData<List<GenreModel>> = genres
}