package com.fa.beatify.pages.music_categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fa.beatify.models.GenreModel

class MusicCategoriesVM: ViewModel() {
    private val genreRepo = GenreRepo()

    fun getGenres(): MutableLiveData<List<GenreModel>> = genreRepo.getGenres()
}