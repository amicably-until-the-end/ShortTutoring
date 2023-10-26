package org.softwaremaestro.presenter.teacher_home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_get.entity.QuestionGetResponseVO
import org.softwaremaestro.domain.question_get.usecase.QuestionGetUseCase
import org.softwaremaestro.presenter.util.Util.logError
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(private val questionGetUseCase: QuestionGetUseCase) :
    ViewModel() {

    private val _questions: MutableLiveData<List<QuestionGetResponseVO>> = MutableLiveData()
    val questions: LiveData<List<QuestionGetResponseVO>> get() = _questions

    fun getQuestions() {
        viewModelScope.launch {
            questionGetUseCase.execute()
                .catch { exception ->
                    logError(this@QuestionsViewModel::class.java, exception.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> _questions.value = result.data
                        is BaseResult.Error -> logError(
                            this@QuestionsViewModel::class.java,
                            result.toString()
                        )
                    }
                }
        }
    }

    fun getMyQuestions() {
        viewModelScope.launch {
            questionGetUseCase.getMyQuestions()
                .catch { exception ->
                    logError(this@QuestionsViewModel::class.java, exception.message ?: "")
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> _questions.value = result.data
                        is BaseResult.Error -> logError(
                            this@QuestionsViewModel::class.java,
                            result.toString()
                        )
                    }
                }
        }
    }
}