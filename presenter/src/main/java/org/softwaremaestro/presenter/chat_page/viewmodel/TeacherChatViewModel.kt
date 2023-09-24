package org.softwaremaestro.presenter.chat_page.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TeacherChatViewModel @Inject constructor() : ViewModel() {

    private val _tutoringTime = MutableLiveData<LocalDateTime>()
    val tutoringTime: LiveData<LocalDateTime> get() = _tutoringTime

    private val _tutoringDuration = MutableLiveData<Int>()
    val tutoringDuration: LiveData<Int> get() = _tutoringDuration

    private val _tutoringTimeAndDurationProper = MediatorLiveData<Boolean>()
    val tutoringTimeAndDurationProper: MediatorLiveData<Boolean> get() = _tutoringTimeAndDurationProper

    init {
        with(_tutoringTimeAndDurationProper) {
            addSource(_tutoringTime) {
                value = _tutoringTime.value != null && _tutoringDuration.value != null
            }

            addSource(_tutoringDuration) {
                value = _tutoringTime.value != null && _tutoringDuration.value != null
            }
        }
    }

    fun pickStudent() {

    }

    fun setTutoringTime(time: LocalDateTime) = _tutoringTime.postValue(time)

    fun setTutoringDuration(duration: Int) = _tutoringDuration.postValue(duration)
}