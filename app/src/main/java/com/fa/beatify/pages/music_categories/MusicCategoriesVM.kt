package com.fa.beatify.pages.music_categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fa.beatify.apis.BeatifyRepo
import com.fa.beatify.models.GenreModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicCategoriesVM @Inject constructor(private val beatifyRepo: BeatifyRepo) : ViewModel() {
    private val genres = MutableLiveData<List<GenreModel>>()

    fun allGenres() {
        viewModelScope.launch {
            genres.postValue(beatifyRepo.allGenres())
        }
    }
    fun getGenres(): MutableLiveData<List<GenreModel>> = genres
}