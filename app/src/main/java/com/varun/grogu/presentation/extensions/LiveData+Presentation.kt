package com.varun.grogu.presentation.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * RxJava type <a href="http://reactivex.io/documentation/operators/debounce.html">debounce</a>
 * it only emit an item from an LiveData if a particular time-span has passed without it emitting another item.
 *
 * Default debounce duration 500ms
 */
fun <T> LiveData<T>.debounce(duration: Long = 500L, coroutineScope: CoroutineScope): LiveData<T> = MediatorLiveData<T>().also { mld ->
    var job: Job? = null
    mld.addSource(this) {
        job?.cancel()
        job = coroutineScope.launch {
            delay(duration)
            mld.value = value
        }
    }
}