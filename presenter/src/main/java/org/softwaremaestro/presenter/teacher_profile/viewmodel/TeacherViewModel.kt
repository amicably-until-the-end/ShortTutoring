package org.softwaremaestro.presenter.teacher_profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.softwaremaestro.domain.teacher_get.entity.TeacherVO
import javax.inject.Inject

@HiltViewModel
class TeacherViewModel @Inject constructor() : ViewModel() {
    private val _teachers: MutableLiveData<List<TeacherVO>> = MutableLiveData()
    val teachers: LiveData<List<TeacherVO>> get() = _teachers

    fun getTeachers() {
        _teachers.postValue(
            mutableListOf<TeacherVO>()
//                .apply {
//                add(
//                    TeacherVO(
//                        "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Circle-icons-profile.svg/2048px-Circle-icons-profile.svg.png",
//                        "목업데이터입니다",
//                        "hc-teacher-id",
//                        "풀 수 없는 문제는 없다.",
//                        "성균관대학교",
//                        4.9f,
//                        listOf(),
//                        -1
//                    )
//                )
//            }
        )
    }

}