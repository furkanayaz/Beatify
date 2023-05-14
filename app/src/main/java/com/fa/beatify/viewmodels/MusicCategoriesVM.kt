package com.fa.beatify.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fa.beatify.models.GenreModel
import com.fa.beatify.repositories.GenreRepo

class MusicCategoriesVM: ViewModel() {
    private val genreRepo = GenreRepo()

    fun getGenres(): MutableLiveData<List<GenreModel>> = genreRepo.getGenres()
}