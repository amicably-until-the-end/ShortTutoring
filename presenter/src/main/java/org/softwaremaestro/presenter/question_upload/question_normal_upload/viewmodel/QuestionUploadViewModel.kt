package org.softwaremaestro.presenter.question_upload.question_normal_upload.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_upload.entity.QuestionUploadResultVO
import org.softwaremaestro.domain.question_upload.entity.QuestionUploadVO
import org.softwaremaestro.domain.question_upload.usecase.QuestionUploadUseCase
import org.softwaremaestro.presenter.util.UIState
import org.softwaremaestro.presenter.util.widget.TimePickerBottomDialog.SpecificTime
import javax.inject.Inject


@HiltViewModel
class QuestionUploadViewModel @Inject constructor(private val questionUploadUseCase: QuestionUploadUseCase) :
    ViewModel() {

    private val _questionUploadState: MutableLiveData<UIState<QuestionUploadResultVO>> =
        MutableLiveData()
    val questionUploadState: LiveData<UIState<QuestionUploadResultVO>> get() = _questionUploadState

    val _description: MutableLiveData<String?> = MutableLiveData()
    val _school: MutableLiveData<String?> = MutableLiveData()
    val _subject: MutableLiveData<String?> = MutableLiveData()
    val _difficulty: MutableLiveData<String?> = MutableLiveData()
    private val _hopeTutoringTime: MutableLiveData<MutableList<SpecificTime>?> =
        MutableLiveData(mutableListOf())
    val _images: MutableLiveData<List<Bitmap>?> = MutableLiveData()
    val _imagesBase64: MutableLiveData<List<String>?> = MutableLiveData()
    private var _inputProper = MediatorLiveData<Boolean>()


    val description: LiveData<String?> get() = _description
    val school: LiveData<String?> get() = _school
    val subject: LiveData<String?> get() = _subject
    val difficulty: LiveData<String?> get() = _difficulty
    val hopeTutoringTime: LiveData<MutableList<SpecificTime>?> get() = _hopeTutoringTime
    val images: LiveData<List<Bitmap>?> get() = _images
    val imagesBase64: LiveData<List<String>?> get() = _imagesBase64
    val inputProper: MediatorLiveData<Boolean> get() = _inputProper

    val inputs = listOf(
        _description, _school, _subject, _imagesBase64, _hopeTutoringTime
    )

    init {
        _images.postValue(listOf())

        with(_inputProper) {
            val allOfInputsNotNull = {
                !description.value.isNullOrEmpty()
                        && !school.value.isNullOrEmpty()
                        && !subject.value.isNullOrEmpty()
                        && !imagesBase64.value.isNullOrEmpty()
                        && !hopeTutoringTime.value.isNullOrEmpty()
            }

            inputs.forEach {
                addSource(it) { postValue(allOfInputsNotNull()) }
            }
        }
    }

    fun uploadQuestion(questionUploadVO: QuestionUploadVO) {

        viewModelScope.launch {
            questionUploadUseCase.execute(questionUploadVO)
                .onStart {
                    _questionUploadState.value = UIState.Loading
                }
                .catch { _ ->
                    _questionUploadState.value = UIState.Failure
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> {
                            _questionUploadState.value = UIState.Success(result.data)
                        }

                        is BaseResult.Error -> _questionUploadState.value = UIState.Failure
                    }
                }


        }
    }

    fun addHopeTutoringTime(time: SpecificTime, onDuplicate: () -> Unit) {
        val times = _hopeTutoringTime.value ?: return
        if (times.contains(time)) {
            onDuplicate()
        } else {
            times.add(time)
        }
        _hopeTutoringTime.postValue(times)
    }

    fun removeHopeTutoringTime(time: SpecificTime) {
        val times = _hopeTutoringTime.value ?: return
        times.remove(time)
        _hopeTutoringTime.postValue(times)
    }

    fun resetInputs() {
        inputs.forEach { it.postValue(null) }
        _hopeTutoringTime.postValue(mutableListOf())
    }
}