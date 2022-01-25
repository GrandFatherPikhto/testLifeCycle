package com.grandfatherpikhto.testlifecycle

import android.content.Context
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.*

@DelicateCoroutinesApi
@InternalCoroutinesApi
class TestRepository(
    private val context: Context,
    private val lifecycle: Lifecycle
): DefaultLifecycleObserver {
    companion object {
        const val TAG:String = "TestRepository"
    }

    private var onPlay  = true

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Log.e(TAG, "onResume($this)")
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Log.e(TAG, "onPause($this)")
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Log.e(TAG, "onCreate($this)")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        onPlay = false
        Log.e(TAG, "onDestroy($this)")
    }

    init {
        Log.e(TAG, "Init $this")
        lifecycle.addObserver(this)
        lifecycle.coroutineScope.launch {
            TestCallback.counter.collect { counter ->
                Log.e(TAG, "$this $counter")
            }
        }
    }

    fun test() {
        Log.e(TAG, "test($this)")
        lifecycle.coroutineScope.launch {
            while (onPlay) {
                TestCallback.incCounter()
                delay(1000)
            }
        }
    }
}