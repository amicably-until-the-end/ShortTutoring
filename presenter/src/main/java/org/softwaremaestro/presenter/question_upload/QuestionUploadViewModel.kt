package org.softwaremaestro.presenter.question_upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.softwaremaestro.domain.question_upload.usecase.QuestionUploadUseCase
import javax.inject.Inject


@HiltViewModel
class QuestionUploadViewModel @Inject constructor(private val questionUploadUseCase: QuestionUploadUseCase) :
    ViewModel() {
    //private var title:String =""
    //private var detail:String = ""
    val schoolSelected_: MutableLiveData<String> = MutableLiveData("고등학교")
    val schoolSelected: LiveData<String> get() = schoolSelected_

    val chapterSelected_: MutableLiveData<String> = MutableLiveData();
    val chapterSelected: LiveData<String> get() = chapterSelected_

    val subjectCodeSelected_: MutableLiveData<Int> = MutableLiveData()

    val subjectCodeSelected: LiveData<Int> get() = subjectCodeSelected_


    /*fun uploadQuestion(questionUploadVO: QuestionUploadVO) {
        viewModelScope.launch {
            questionUploadUseCase.execute(questionUploadVO)
            )
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
    }*/

}