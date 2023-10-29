package org.softwaremaestro.presenter.chat_page.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.socket.client.Socket
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_upload.usecase.TeacherPickUseCase
import org.softwaremaestro.presenter.util.UIState
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class StudentChatViewModel @Inject constructor(
    private val teacherPickUseCase: TeacherPickUseCase
) :
    ViewModel() {

    private var socket: Socket? = null

    private val pickTeacherResult = MutableLiveData<UIState<Boolean>>()
    val pickTeacherResultState: LiveData<UIState<Boolean>> get() = pickTeacherResult

    private val _tutoringTime = MutableLiveData<LocalDateTime?>()
    val tutoringTime: LiveData<LocalDateTime?> get() = _tutoringTime

    private val _tutoringDuration = MutableLiveData<Int?>()
    val tutoringDuration: LiveData<Int?> get() = _tutoringDuration

    fun setTutoringTime(time: LocalDateTime?) = _tutoringTime.postValue(time)

    fun setTutoringDuration(duration: Int?) = _tutoringDuration.postValue(duration)

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

    fun pickTeacher(
        chattingId: String,
        questionId: String,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ) {
        viewModelScope.launch {
            teacherPickUseCase.execute(startTime, endTime, chattingId, questionId)
                .onStart { pickTeacherResult.value = UIState.Loading }
                .catch { exception ->
                    pickTeacherResult.value = UIState.Failure
                    Log.e(this@StudentChatViewModel::class.java.name, exception.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> {
                            pickTeacherResult.value = UIState.Success(true)
                        }

                        is BaseResult.Error -> {
                            pickTeacherResult.value = UIState.Failure
                        }
                    }
                }
        }
    }

}