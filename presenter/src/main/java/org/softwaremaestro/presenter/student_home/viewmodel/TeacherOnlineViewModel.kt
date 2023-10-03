package org.softwaremaestro.presenter.student_home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.profile.usecase.MyProfileGetUseCase
import org.softwaremaestro.domain.teacher_get.entity.TeacherVO
import javax.inject.Inject

@HiltViewModel
class TeacherOnlineViewModel @Inject constructor(private val myProfileGetUseCase: MyProfileGetUseCase) :
    ViewModel() {

    private val _teacherOnlines: MutableLiveData<List<TeacherVO>> = MutableLiveData()
    val teacherOnlines: LiveData<List<TeacherVO>> get() = _teacherOnlines

    fun getTeacherOnlines() {
        viewModelScope.launch {
            _teacherOnlines.postValue(mutableListOf<TeacherVO>().apply {
                repeat(3) {
                    add(
                        TeacherVO(
                            profileUrl = "",
                            nickname = "더미데이터${it}",
                            teacherId = "hc-teacher-id",
                            bio = "더미데이터입니다",
                            univ = "한양대학교 수학교육과",
                            rating = -1.0f,
                            listOf(),
                            -1,
                        )
                    )
                }
            })
        }
    }
}