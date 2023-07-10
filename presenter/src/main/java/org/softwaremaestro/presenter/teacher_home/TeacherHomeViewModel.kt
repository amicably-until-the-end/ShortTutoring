package org.softwaremaestro.presenter.teacher_home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_get.entity.QuestionGetResultVO
import org.softwaremaestro.domain.question_get.usecase.QuestionGetUseCase
import javax.inject.Inject

@HiltViewModel
class TeacherHomeViewModel @Inject constructor(private val getQuestion: QuestionGetUseCase): ViewModel() {

    private val _questions: MutableLiveData<List<QuestionGetResultVO>> = MutableLiveData()
    val questions: LiveData<List<QuestionGetResultVO>> get() = _questions

    fun getQuestions() {
        viewModelScope.launch {
            getQuestion()
                .catch { exception ->
                    // Todo: 추후에 에러 어떻게 처리할지 생각해보기
                    Log.d("Error", exception.message.toString())
                }
                .collect { result ->
                    _questions.value = when (result) {
                        is BaseResult.Success -> {
                            result.data
                        }
                        is BaseResult.Error -> emptyList()
                    }
                }
        }
    }
}