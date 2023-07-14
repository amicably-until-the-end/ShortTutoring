package org.softwaremaestro.presenter.question_upload

import android.util.Log
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


    private val _questionId: MutableLiveData<String?> = MutableLiveData();
    val questionId: LiveData<String?> get() = _questionId


    fun uploadQuestion(questionUploadVO: QuestionUploadVO) {
        viewModelScope.launch {
            questionUploadUseCase.execute(questionUploadVO)
                .catch { exception ->

                    _questionId.value = null
                }
                .collect { result ->
                    Log.d("mymymy", "${result.toString()} is result in viewmodel")
                    when (result) {
                        is BaseResult.Success -> {
                            _questionId.value = result.data.question_id
                        }

                        is BaseResult.Error -> _questionId.value = null
                    }
                }

        }
    }

}