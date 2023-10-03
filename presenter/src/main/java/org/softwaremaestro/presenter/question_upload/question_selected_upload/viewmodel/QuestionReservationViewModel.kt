package org.softwaremaestro.presenter.question_upload.question_selected_upload.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class QuestionReservationViewModel @Inject constructor() : ViewModel() {

    private val _requestDate = MutableLiveData<LocalDate?>()
    val requestDate: LiveData<LocalDate?> get() = _requestDate

    private val _requestTutoringStartTime = MutableLiveData<LocalTime?>()
    val requestTutoringStartTime: LiveData<LocalTime?> get() = _requestTutoringStartTime

    private val _requestTutoringEndTime = MutableLiveData<LocalTime?>()
    val requestTutoringEndTime: LiveData<LocalTime?> get() = _requestTutoringEndTime

    private val inputs = listOf(
        _requestDate,
        _requestTutoringStartTime,
        _requestTutoringEndTime
    )

    private var _inputProper = MediatorLiveData<Boolean>()
    val inputProper: MediatorLiveData<Boolean> get() = _inputProper

    init {
        with(_inputProper) {

            val allOfInputsNotNull = { !inputs.map { it.value != null }.contains(false) }

            inputs.forEach {
                addSource(it) { postValue(allOfInputsNotNull()) }
            }
        }
    }

    fun setRequestDate(requestDate: LocalDate?) =
        _requestDate.postValue(requestDate)

    fun setRequestTutoringStartTime(requestTutoringStartTime: LocalTime?) =
        _requestTutoringStartTime.postValue(requestTutoringStartTime)


    fun setRequestTutoringEndTime(requestTutoringEndTime: LocalTime?) =
        _requestTutoringEndTime.postValue(requestTutoringEndTime)

    fun resetInputs() = inputs.forEach { it.postValue(null) }
}