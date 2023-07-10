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
    //private var title:String =""
    //private var detail:String = ""

    private val _result: MutableLiveData<String> = MutableLiveData();
    val resultString: LiveData<String> get() = _result


    fun uploadQuestion(detail: String) {
        viewModelScope.launch {
            questionUploadUseCase.execute(QuestionUploadVO("title is title", detail))
                .catch { exception ->
                    _result.value = exception.message.toString()
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> _result.value = "success"
                        is BaseResult.Error -> _result.value = "fail"
                    }
                }
        }
    }

}