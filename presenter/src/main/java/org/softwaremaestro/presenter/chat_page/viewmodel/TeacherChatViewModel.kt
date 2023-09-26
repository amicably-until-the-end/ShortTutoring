package org.softwaremaestro.presenter.chat_page.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.answer_upload.usecase.StudentPickUseCase
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.presenter.util.UIState
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TeacherChatViewModel @Inject constructor(
    private val studentPickUseCase: StudentPickUseCase
) : ViewModel() {

    private val _tutoringTime = MutableLiveData<LocalDateTime>()
    val tutoringTime: LiveData<LocalDateTime> get() = _tutoringTime

    private val _tutoringDuration = MutableLiveData<Int>()
    val tutoringDuration: LiveData<Int> get() = _tutoringDuration

    private val _tutoringTimeAndDurationProper = MediatorLiveData<Boolean>()
    val tutoringTimeAndDurationProper: MediatorLiveData<Boolean> get() = _tutoringTimeAndDurationProper

    private val _pickStudentResult = MutableLiveData<UIState<Boolean>>()
    val pickStudentResult: LiveData<UIState<Boolean>> get() = _pickStudentResult

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

    fun pickStudent(questionId: String) {
        viewModelScope.launch {
            studentPickUseCase.execute(questionId)
                .onStart { _pickStudentResult.value = UIState.Loading }
                .catch { exception ->
                    _pickStudentResult.value = UIState.Failure
                    Log.e(this@TeacherChatViewModel::class.java.name, exception.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> {
                            _pickStudentResult.value = UIState.Success(true)
                        }

                        is BaseResult.Error -> {
                            _pickStudentResult.value = UIState.Failure
                        }
                    }
                }
        }
    }

    fun setTutoringTime(time: LocalDateTime) = _tutoringTime.postValue(time)

    fun setTutoringDuration(duration: Int) = _tutoringDuration.postValue(duration)
}