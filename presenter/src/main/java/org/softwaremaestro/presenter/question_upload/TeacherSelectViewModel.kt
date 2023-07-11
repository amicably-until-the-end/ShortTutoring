package org.softwaremaestro.presenter.question_upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.softwaremaestro.domain.question_upload.entity.TeacherVO
import org.softwaremaestro.domain.question_upload.usecase.QuestionUploadUseCase
import javax.inject.Inject


@HiltViewModel
class TeacherSelectViewModel @Inject constructor(private val questionUploadUseCase: QuestionUploadUseCase) :
    ViewModel() {


    private val _teacherList: MutableLiveData<List<TeacherVO>> = MutableLiveData();
    val teacherList: LiveData<List<TeacherVO>> get() = _teacherList


}