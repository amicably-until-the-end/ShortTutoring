package org.softwaremaestro.presenter.question_upload.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
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
import org.softwaremaestro.presenter.Util.UIState
import org.softwaremaestro.presenter.Util.toBase64
import javax.inject.Inject


@HiltViewModel
class QuestionUploadViewModel @Inject constructor(private val questionUploadUseCase: QuestionUploadUseCase) :
    ViewModel() {


    private val _questionUploadState: MutableLiveData<UIState<QuestionUploadResultVO>> =
        MutableLiveData();
    val questionUploadState: LiveData<UIState<QuestionUploadResultVO>> get() = _questionUploadState

    val _description: MutableLiveData<String?> = MutableLiveData();
    val _school: MutableLiveData<String?> = MutableLiveData();
    val _subject: MutableLiveData<String?> = MutableLiveData();
    val _difficulty: MutableLiveData<String?> = MutableLiveData();
    val _images: MutableLiveData<List<Bitmap>?> = MutableLiveData();


    val description: LiveData<String?> get() = _description
    val school: LiveData<String?> get() = _school
    val subject: LiveData<String?> get() = _subject
    val difficulty: LiveData<String?> get() = _difficulty
    val images: LiveData<List<Bitmap>?> get() = _images


    init {
        _images.postValue(listOf())
    }

    fun uploadQuestion() {
        val questionUploadVO = QuestionUploadVO(
            studentId = "test-student-id",
            imageBase64 = images.value?.get(0)?.toBase64() ?: "",
            imageFormat = "png",
            description = description.value ?: "",
            schoolLevel = school.value ?: "",
            schoolSubject = subject.value ?: "",
            difficulty = difficulty.value ?: "",
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