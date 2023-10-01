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
import org.softwaremaestro.domain.answer_upload.usecase.DeclineQuestionUseCase
import org.softwaremaestro.domain.answer_upload.usecase.StudentPickUseCase
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.presenter.util.UIState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class TeacherChatViewModel @Inject constructor(
    private val studentPickUseCase: StudentPickUseCase,
    private val declineQuestionUseCase: DeclineQuestionUseCase
) : ViewModel() {

    var tutoringDuration: Int? = null
    var tutoringDate: LocalDate? = null
    var tutoringStart: LocalTime? = null


    private val _tutoringTimeAndDurationProper = MediatorLiveData<Boolean>()
    val tutoringTimeAndDurationProper: MediatorLiveData<Boolean> get() = _tutoringTimeAndDurationProper

    private val _pickStudentResult = MutableLiveData<UIState<Boolean>>()
    val pickStudentResult: LiveData<UIState<Boolean>> get() = _pickStudentResult

    fun pickStudent(questionId: String, chattingId: String) {
        try {
            var startTime = LocalDateTime.of(tutoringDate, tutoringStart)
            var endTime = startTime?.plusMinutes(tutoringDuration!!.toLong())!!
            viewModelScope.launch {
                Log.d(
                    "TeacherChatViewModel",
                    "pickStudent: $questionId, $startTime, $endTime, $chattingId"
                )
                studentPickUseCase.execute(questionId, startTime, endTime, chattingId)
                    .onStart { _pickStudentResult.value = UIState.Loading }
                    .catch { exception ->
                        _pickStudentResult.value = UIState.Failure
                        Log.e(
                            this@TeacherChatViewModel::class.java.name,
                            exception.message.toString()
                        )
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
        } catch (e: Exception) {
            Log.d("TeacherChatViewModel", "pickStudent: $e")
        }
    }


    fun declineQuestion(tutoringId: String) {
        viewModelScope.launch {
            declineQuestionUseCase.execute(tutoringId)
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
}