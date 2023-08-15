package com.fa.beatify.presentation.music_categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fa.beatify.data.response.BeatifyResponse
import com.fa.beatify.domain.models.Genre
import com.fa.beatify.domain.remote.use_cases.AllGenresUseCase
import com.fa.beatify.utils.network.Connection
import com.fa.beatify.utils.network.NetworkConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MusicCategoriesVM(
    networkConnection: NetworkConnection,
    private val allGenresUseCase: AllGenresUseCase
) : ViewModel() {
    private var _connObserver: Flow<Connection.Status>? = null

    private val connObserver: Flow<Connection.Status> get() = _connObserver!!

    private var _genres: MutableLiveData<BeatifyResponse<List<Genre>>>? = null
    val genres: MutableLiveData<BeatifyResponse<List<Genre>>>
        get() = _genres!!

    private var allGenresJob: Job? = null

    init {
        _connObserver = networkConnection.observe()
        _genres = MutableLiveData<BeatifyResponse<List<Genre>>>()
    }

    fun fetchData() {
        _genres?.postValue(BeatifyResponse.Loading())

        viewModelScope.launch(context = Dispatchers.Unconfined) {
            connObserver.collect {
                when (it) {
                    Connection.Status.Available -> allGenres()
                    Connection.Status.Losing -> _genres?.postValue(BeatifyResponse.Loading())
                    Connection.Status.Unavailable, Connection.Status.Lost -> _genres?.postValue(
                        BeatifyResponse.Failure(code = 404)
                    )
                }
            }
        }
    }

    private fun allGenres() {
        allGenresJob = viewModelScope.launch(context = Dispatchers.IO) {
            try {
                allGenresUseCase().also { genreList: List<Genre> ->
                    _genres?.postValue(BeatifyResponse.Success(genreList, code = 200))
                }
            } catch (e: Exception) {
                _genres?.postValue(BeatifyResponse.Failure(code = 404))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        allGenresJob = null

        _connObserver = null
        _genres = null
    }

}