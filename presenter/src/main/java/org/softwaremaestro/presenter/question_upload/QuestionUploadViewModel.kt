package org.softwaremaestro.presenter.question_upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_upload.entity.QuestionUploadVO
import org.softwaremaestro.domain.question_upload.usecase.QuestionUploadUseCase
import javax.inject.Inject


@HiltViewModel
class QuestionUploadViewModel @Inject constructor(private val questionUploadUseCase: QuestionUploadUseCase) :
    ViewModel() {


    private val _uploadResult: MutableLiveData<String> = MutableLiveData();
    val uploadResult: LiveData<String> get() = _uploadResult


    fun uploadQuestion(questionUploadVO: QuestionUploadVO) {
        viewModelScope.launch {
            questionUploadUseCase.execute(questionUploadVO)
                .catch { exception -> _uploadResult.value = exception.message.toString() }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> _uploadResult.value = "success"
                        is BaseResult.Error -> _uploadResult.value = "fail"
                    }
                }

        }
    }

}