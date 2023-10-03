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

    private val _teacherId = MutableLiveData<String?>()
    val teacherId: LiveData<String?> get() = _teacherId

    private val _description = MutableLiveData<String?>()
    val description: LiveData<String?> get() = _description

    private var _schoolLevel = MutableLiveData<String?>()
    val schoolLevel: LiveData<String?> get() = _schoolLevel

    private var _schoolSubject = MutableLiveData<String?>()
    val schoolSubject: LiveData<String?> get() = _schoolSubject

    private var _mainImageIndex = MutableLiveData<Int>()
    val mainImageIndex: LiveData<Int> get() = _mainImageIndex

    private var _images = MutableLiveData<List<Bitmap>?>()
    val images: LiveData<List<Bitmap>?> get() = _images

    private val inputs = listOf(
        _description,
        _schoolLevel,
        _schoolSubject
    )

    private var _inputProper = MediatorLiveData<Boolean>()
    val inputProper: MediatorLiveData<Boolean> get() = _inputProper

    private val _questionUploadState = MutableLiveData<UIState<QuestionSelectedUploadResultVO>>()
    val questionUploadState: LiveData<UIState<QuestionSelectedUploadResultVO>> get() = _questionUploadState

    init {
        with(_inputProper) {
            val allOfInputsNotNull = { !inputs.map { it.value != null }.contains(false) }

            inputs.forEach {
                addSource(it) { postValue(allOfInputsNotNull()) }
            }
        }
    }

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

    fun setDescription(description: String?) = _description.postValue(description)

    fun setSchoolLevel(schoolLevel: String?) {
        _schoolLevel.value = schoolLevel
    }

    fun setSchoolSubject(schoolSubject: String?) = _schoolSubject.postValue(schoolSubject)

    fun setMainImageIndex(mainImageIndex: Int) = _mainImageIndex.postValue(mainImageIndex)

    fun setImages(images: List<Bitmap>?) = _images.postValue(images)

    fun setTeacherId(teacherId: String?) = _teacherId.postValue(teacherId)

    fun resetInputs() = inputs.forEach { it.postValue(null) }
}