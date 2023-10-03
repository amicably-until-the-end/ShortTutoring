package org.softwaremaestro.presenter.chat_page.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_get.entity.QuestionGetResponseVO
import org.softwaremaestro.domain.question_get.usecase.QuestionGetUseCase
import javax.inject.Inject

@HiltViewModel
class QuestionImageViewModel @Inject constructor(
    private val questionGetUseCase: QuestionGetUseCase
) :
    ViewModel() {

    private val _questionInfo = MutableLiveData<QuestionGetResponseVO>()
    val questionInfo: MutableLiveData<QuestionGetResponseVO> get() = _questionInfo


    fun getImages(questionId: String) {
        viewModelScope.launch {
            questionGetUseCase.getQuestionInfo(questionId)
                .catch {

                }
                .collect {
                    when (it) {
                        is BaseResult.Success -> {
                            _questionInfo.postValue(it.data)
                        }

                        is BaseResult.Error -> {

                        }
                    }
                }
        }
    }

}