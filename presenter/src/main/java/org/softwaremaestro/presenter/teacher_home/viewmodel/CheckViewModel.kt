package org.softwaremaestro.presenter.teacher_home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_check.entity.QuestionCheckRequestVO
import org.softwaremaestro.domain.question_check.entity.QuestionCheckResultVO
import org.softwaremaestro.domain.question_check.usecase.QuestionCheckUseCase
import org.softwaremaestro.domain.question_get.entity.QuestionGetResultVO
import org.softwaremaestro.domain.question_get.usecase.QuestionGetUseCase
import javax.inject.Inject

@HiltViewModel
class CheckViewModel @Inject constructor(private val questionCheckUseCase: QuestionCheckUseCase) :
    ViewModel() {

    private val _check: MutableLiveData<QuestionCheckResultVO> = MutableLiveData()
    val check: LiveData<QuestionCheckResultVO> get() = _check

    fun checkQuestion(requestId: String, questionCheckRequestVO: QuestionCheckRequestVO) {
        viewModelScope.launch {
            questionCheckUseCase.execute(requestId, questionCheckRequestVO)
                .catch { exception ->
                    // Todo: 추후에 에러 어떻게 처리할지 생각해보기
                    Log.d("Error", exception.message.toString())
                }
                .collect { result ->
                    Log.d("Result", result.toString())
                    when (result) {
                        is BaseResult.Success -> _check.value = result.data
                        is BaseResult.Error -> Log.d("Error", result.toString())
                    }
                }
        }
    }
}