package com.fa.beatify.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainVM: ViewModel() {
    private val condition: MutableStateFlow<Boolean> = MutableStateFlow(value = true)

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch(context = Dispatchers.IO) {
            delay(timeMillis = 250L)
            condition.value = false
        }
    }

    fun getCondition(): Boolean {
        return condition.value
    }

}