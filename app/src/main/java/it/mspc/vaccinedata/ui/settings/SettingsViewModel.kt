package it.mspc.vaccinedata.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SettingsViewModel {

    private val _text = MutableLiveData<String>().apply {
        value = "This is settings Fragment"
    }
    val text: LiveData<String> = _text
}