package org.softwaremaestro.presenter.classroom.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.classroom.usecase.FinishClassUseCase
import org.softwaremaestro.domain.classroom.usecase.GetTutoringInfoUseCase
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_get.entity.QuestionGetResponseVO
import org.softwaremaestro.domain.question_get.usecase.QuestionGetUseCase
import javax.inject.Inject

@HiltViewModel
class ClassroomViewModel @Inject constructor(
    private val finishClassUseCase: FinishClassUseCase,
    private val getTutoringInfoUseCase: GetTutoringInfoUseCase,
    private val questionGetUseCase: QuestionGetUseCase,
) :
    ViewModel() {


    private val _finishClassResult = MutableLiveData<String>()
    val finishClassResult: LiveData<String> = _finishClassResult

    private val _questionInfo = MutableLiveData<QuestionGetResponseVO>()
    val questionInfo: LiveData<QuestionGetResponseVO> = _questionInfo

    fun finishClass(tutoringId: String) {
        viewModelScope.launch {
            finishClassUseCase.execute(tutoringId)
                .onStart {

                }
                .catch { e ->
                    Log.e("${this@ClassroomViewModel::class.java}", e.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> {
                            _finishClassResult.postValue("finished")
                        }

                        is BaseResult.Error -> {
                            _finishClassResult.postValue("error")
                        }
                    }
                }
        }
    }

    fun getQuestionInfo(questionId: String) {
        viewModelScope.launch {
            questionGetUseCase.getQuestionInfo(questionId)
                .onStart {

                }
                .catch { e ->
                    Log.e("${this@ClassroomViewModel::class.java}", e.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> {
                            _questionInfo.postValue(result.data)
                        }

                        else -> {

                        }

                    }
                }
        }
    }


}