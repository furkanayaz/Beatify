package com.fa.beatify.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fa.beatify.repositories.MusicPlayerRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainVM: ViewModel() {
    private val condition: MutableStateFlow<Boolean> = MutableStateFlow(value = true)
    private val musicPlayerRepo = MusicPlayerRepo()

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch(context = Dispatchers.IO) {
            delay(timeMillis = 5000L)
            condition.value = false
        }
    }

    fun getCondition(): Boolean {
        return condition.value
    }

    fun getPlayingController(): MutableStateFlow<Boolean> = musicPlayerRepo.getPlayingController()

}