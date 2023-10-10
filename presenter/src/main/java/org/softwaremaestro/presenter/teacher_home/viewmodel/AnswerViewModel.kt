package org.softwaremaestro.presenter.teacher_home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.answer_upload.entity.AnswerUploadResVO
import org.softwaremaestro.domain.answer_upload.entity.AnswerUploadVO
import org.softwaremaestro.domain.answer_upload.usecase.AnswerUploadUseCase
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.presenter.util.Util.logError
import javax.inject.Inject

@HiltViewModel
class AnswerViewModel @Inject constructor(private val answerUploadUseCase: AnswerUploadUseCase) :
    ViewModel() {

    private val _offerResult: MutableLiveData<AnswerUploadResVO?> = MutableLiveData()
    val offerResult: LiveData<AnswerUploadResVO?> get() = _offerResult

    fun uploadAnswer(answerUploadVO: AnswerUploadVO) {
        viewModelScope.launch {
            answerUploadUseCase.execute(answerUploadVO)
                .catch { exception ->
                    _offerResult.value = null
                    logError(this@AnswerViewModel::class.java, exception.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> _offerResult.value = result.data
                        is BaseResult.Error -> logError(
                            this@AnswerViewModel::class.java,
                            result.toString()
                        )
                    }
                }
        }
    }
}