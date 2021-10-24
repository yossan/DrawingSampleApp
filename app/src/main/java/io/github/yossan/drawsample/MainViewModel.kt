package io.github.yossan.drawsample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    val _mode = MutableLiveData<DrawView.Mode>(DrawView.Mode.pen)
    val mode = MutableLiveData<DrawView.Mode>(DrawView.Mode.pen)
    fun toggleMode() {
        if (mode.value == DrawView.Mode.pen) {
            mode.value = DrawView.Mode.eraser
        } else {
            mode.value = DrawView.Mode.pen
        }
    }
}