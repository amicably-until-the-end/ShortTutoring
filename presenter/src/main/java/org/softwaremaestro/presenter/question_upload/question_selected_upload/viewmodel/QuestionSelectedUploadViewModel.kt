package org.softwaremaestro.presenter.question_upload.question_selected_upload.viewmodel

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
import org.softwaremaestro.domain.question_selected_upload.entity.QuestionSelectedUploadResultVO
import org.softwaremaestro.domain.question_selected_upload.entity.QuestionSelectedUploadVO
import org.softwaremaestro.domain.question_selected_upload.usecase.QuestionSelectedUploadUseCase
import org.softwaremaestro.presenter.util.UIState
import javax.inject.Inject

@HiltViewModel
class QuestionSelectedUploadViewModel @Inject constructor(
    private val questionSelectedUploadUseCase: QuestionSelectedUploadUseCase
) :
    ViewModel() {

    private val _questionUploadState = MutableLiveData<UIState<QuestionSelectedUploadResultVO>>()
    val questionUploadState: LiveData<UIState<QuestionSelectedUploadResultVO>> get() = _questionUploadState

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> get() = _description

    private var _schoolLevel = MutableLiveData<String>()
    val schoolLevel: LiveData<String> get() = _schoolLevel

    private var _schoolSubject = MutableLiveData<String>()
    val schoolSubject: LiveData<String> get() = _schoolSubject

    private var _mainImageIndex = MutableLiveData<Int>()
    val mainImageIndex: LiveData<Int> get() = _mainImageIndex

    private var _images = MutableLiveData<List<Bitmap>>()
    val images: LiveData<List<Bitmap>> get() = _images

    private var _requestTutoringStartTime = MutableLiveData<List<String>?>()
    val requestTutoringStartTime: LiveData<List<String>?> get() = _requestTutoringStartTime

    private var _requestTutoringEndTime = MutableLiveData<List<String>?>()
    val requestTutoringEndTime: LiveData<List<String>?> get() = _requestTutoringEndTime

    private var _requestTeacherId = MutableLiveData<String>()
    val requestTeacherId: LiveData<String> get() = _requestTeacherId

    private var _startTimeAndEndTimeProper = MediatorLiveData<Boolean>()
    val startTimeAndEndTimeProper: MediatorLiveData<Boolean> get() = _startTimeAndEndTimeProper

    init {
        with(_startTimeAndEndTimeProper) {
            addSource(_requestTutoringStartTime) {
                postValue(_requestTutoringStartTime.value != null && _requestTutoringEndTime.value != null)
            }

            addSource(_requestTutoringEndTime) {
                postValue(_requestTutoringStartTime.value != null && _requestTutoringEndTime.value != null)
            }
        }
    }

    fun setDescription(description: String) = _description.postValue(description)

    fun setSchoolLevel(schoolLevel: String) {
        _schoolLevel.value = schoolLevel
    }

    fun setSchoolSubject(schoolSubject: String) = _schoolSubject.postValue(schoolSubject)

    fun setMainImageIndex(mainImageIndex: Int) = _mainImageIndex.postValue(mainImageIndex)

    fun setImages(images: List<Bitmap>) = _images.postValue(images)

    fun setRequestTutoringStartTime(requestTutoringStartTime: List<String>?) =
        _requestTutoringStartTime.postValue(requestTutoringStartTime)


    fun setRequestTutoringEndTime(requestTutoringEndTime: List<String>?) =
        _requestTutoringEndTime.postValue(requestTutoringEndTime)

    fun setRequestTeacherId(requestTeacherId: String) =
        _requestTeacherId.postValue(requestTeacherId)


    fun uploadQuestionSelected(questionSelectedUploadVO: QuestionSelectedUploadVO) {
        viewModelScope.launch {
            questionSelectedUploadUseCase.execute(questionSelectedUploadVO)
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