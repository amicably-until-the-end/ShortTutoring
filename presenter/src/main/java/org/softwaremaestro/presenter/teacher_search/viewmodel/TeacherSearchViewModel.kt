package org.softwaremaestro.presenter.teacher_search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.softwaremaestro.domain.best_teacher_get.entity.TeacherVO
import javax.inject.Inject

@HiltViewModel
class TeacherSearchViewModel @Inject constructor() : ViewModel() {

    private val _searchedResult: MutableLiveData<List<TeacherVO>> = MutableLiveData()
    val searchedResult: LiveData<List<TeacherVO>> get() = _searchedResult

    fun searchTeacherByName(name: String) {
        _searchedResult.postValue(mutableListOf<TeacherVO>().apply {
            repeat(100) {
                add(
                    TeacherVO(
                        profileUrl = "",
                        nickname = "더미데이터입니다",
                        teacherId = "hc-teacher-id",
                        bio = "더미데이터입니다",
                        univ = "피식대학교",
                        rating = -1F,
                        followers = listOf(),
                        reservationCnt = 1,
                    )
                )
            }
        })
    }
}