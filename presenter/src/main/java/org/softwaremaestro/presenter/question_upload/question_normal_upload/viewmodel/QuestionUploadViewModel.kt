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
import java.time.LocalDateTime
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
    var _hopeNow = MutableLiveData<Boolean?>()
    var _hopeTutoringTime = MutableLiveData<MutableList<LocalDateTime>?>(mutableListOf())
    val _images: MutableLiveData<List<Bitmap>?> = MutableLiveData()
    val _imagesBase64: MutableLiveData<List<String>?> = MutableLiveData()


    val description: LiveData<String?> get() = _description
    val school: LiveData<String?> get() = _school
    val subject: LiveData<String?> get() = _subject
    val hopeNow: LiveData<Boolean?> get() = _hopeNow
    val hopeTutoringTime: LiveData<MutableList<LocalDateTime>?> get() = _hopeTutoringTime
    val images: LiveData<List<Bitmap>?> get() = _images
    val imagesBase64: LiveData<List<String>?> get() = _imagesBase64

    private var _inputProper = MediatorLiveData<Boolean>()
    val inputProper: MediatorLiveData<Boolean> get() = _inputProper

    init {
        _images.postValue(listOf())

        val inputs =
            listOf(_description, _school, _subject, _hopeNow, _hopeTutoringTime, _imagesBase64)

        val mInputProper = {
            !_description.value.isNullOrEmpty()
                    && !_school.value.isNullOrEmpty()
                    && !_subject.value.isNullOrEmpty()
                    && (_hopeNow.value == true || !_hopeTutoringTime.value.isNullOrEmpty())
                    && !_imagesBase64.value.isNullOrEmpty()
        }

        with(_inputProper) {
            inputs.forEach { input ->
                addSource(input) {
                    postValue(mInputProper())
                }
            }
        }
    }

    fun uploadQuestion() {

        val mHopeTutoringTime = mutableListOf<LocalDateTime>()
        if (hopeNow.value!!) {
            mHopeTutoringTime.add(LocalDateTime.now())
        }
        mHopeTutoringTime.addAll(hopeTutoringTime.value!!)

        val questionUploadVO = QuestionUploadVO(
            images = imagesBase64.value!!,
            description = description.value!!,
            schoolLevel = school.value!!,
            schoolSubject = subject.value!!,
            hopeImmediate = hopeNow.value!!,
            hopeTutoringTime = mHopeTutoringTime,
            mainImageIndex = 0
        )

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

}