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
class QuestionViewModel @Inject constructor(private val questionGetUseCase: QuestionGetUseCase) :
    ViewModel() {

    private val _questions: MutableLiveData<List<QuestionGetResponseVO>?> = MutableLiveData()
    val questions: LiveData<List<QuestionGetResponseVO>?> get() = _questions

    private val _myQuestions: MutableLiveData<List<QuestionGetResponseVO>?> = MutableLiveData()
    val myQuestions: LiveData<List<QuestionGetResponseVO>?> get() = _myQuestions

    fun getQuestions() {
        viewModelScope.launch {
            questionGetUseCase.getQuestions()
                .catch { exception ->
                    logError(this@QuestionViewModel::class.java, exception.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> _questions.value = result.data
                        is BaseResult.Error -> logError(
                            this@QuestionViewModel::class.java,
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
                    logError(this@QuestionViewModel::class.java, exception.message ?: "")
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> _myQuestions.value = result.data
                        is BaseResult.Error -> logError(
                            this@QuestionViewModel::class.java,
                            result.toString()
                        )
                    }
                }
        }
    }
}