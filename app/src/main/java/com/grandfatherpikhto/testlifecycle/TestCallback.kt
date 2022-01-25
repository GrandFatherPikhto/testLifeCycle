package com.grandfatherpikhto.testlifecycle

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object TestCallback {
    private val sharedCounter = MutableStateFlow<Int>(0)
    val counter get() = sharedCounter.asStateFlow()

    fun incCounter() {
        sharedCounter.tryEmit(sharedCounter.value + 1)
    }
}