package com.infinitegearstudio.skysense.ui.logout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LogoutViewModel : ViewModel() {
    fun updateText(newText: String) {

        _text.value = newText

    }

    private val _text = MutableLiveData<String>().apply {
        value = "Log Out"
    }
    val text: LiveData<String> = _text
}