package com.fa.beatify.presentation.music_categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fa.beatify.data.response.BeatifyResponse
import com.fa.beatify.domain.models.Genre
import com.fa.beatify.domain.remote_use_cases.AllGenresUseCase
import com.fa.beatify.utils.network.Connection
import com.fa.beatify.utils.network.NetworkConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicCategoriesVM @Inject constructor(
    networkConnection: NetworkConnection,
    private val allGenresUseCase: AllGenresUseCase
) : ViewModel() {
    private val _connObserver: Flow<Connection.Status> = networkConnection.observe()
    private val _genres = MutableLiveData<BeatifyResponse<List<Genre>>>()
    val genres: MutableLiveData<BeatifyResponse<List<Genre>>>
        get() = _genres

    fun fetchData() {
        _genres.postValue(BeatifyResponse.Loading())

        viewModelScope.launch(context = Dispatchers.Unconfined) {
            _connObserver.collect {
                when (it) {
                    Connection.Status.Available -> allGenres()
                    Connection.Status.Losing -> _genres.postValue(BeatifyResponse.Loading())
                    Connection.Status.Unavailable, Connection.Status.Lost -> _genres.postValue(
                        BeatifyResponse.Failure(code = 404)
                    )
                }
            }
        }
    }

    private fun allGenres() {
        viewModelScope.launch(context = Dispatchers.IO) {
            try {
                allGenresUseCase().also { genreList: List<Genre> ->
                    _genres.postValue(BeatifyResponse.Success(genreList, code = 200))
                }
            } catch (e: Exception) {
                _genres.postValue(BeatifyResponse.Failure(code = 404))
            }
        }
    }
}