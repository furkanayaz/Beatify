package com.fa.beatify.pages.music_categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fa.beatify.apis.BeatifyRepo
import com.fa.beatify.apis.BeatifyResponse
import com.fa.beatify.models.Genre
import com.fa.beatify.utils.network.Connection
import com.fa.beatify.utils.network.NetworkConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Exception

@HiltViewModel
class MusicCategoriesVM @Inject constructor(networkConnection: NetworkConnection, private val beatifyRepo: BeatifyRepo) : ViewModel() {
    private val _connObserver: Flow<Connection.Status> = networkConnection.observe()
    private val _genres = MutableLiveData<BeatifyResponse<Genre>>()
    val genres: MutableLiveData<BeatifyResponse<Genre>>
        get() = _genres

    init {
        _genres.postValue(BeatifyResponse.Loading())

        viewModelScope.launch {
            _connObserver.collect {
                when(it) {
                    Connection.Status.Available -> allGenres()
                    Connection.Status.Losing -> _genres.postValue(BeatifyResponse.Loading())
                    Connection.Status.Unavailable, Connection.Status.Lost -> _genres.postValue(BeatifyResponse.Failure(code = 404))
                }
            }
        }
    }

    fun allGenres() {
        viewModelScope.launch(context = Dispatchers.IO) {
            try {
                val allGenres: Genre? = beatifyRepo.allGenres()
                allGenres?.let { genre: Genre ->
                    _genres.postValue(BeatifyResponse.Success(genre, code = 200))
                } ?: _genres.postValue(BeatifyResponse.Failure(code = 204))
            } catch (e: Exception) {
                _genres.postValue(BeatifyResponse.Failure(code = 404))
            }
        }
    }
}