package org.softwaremaestro.presenter.student_home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.lecture_get.entity.LectureVO
import javax.inject.Inject

@HiltViewModel
class LectureViewModel @Inject constructor() : ViewModel() {
    private val _lectures = MutableLiveData<List<LectureVO>>()
    val lectures: LiveData<List<LectureVO>> get() = _lectures

    fun getLectures() {
        viewModelScope.launch {
            _lectures.postValue(
                mutableListOf<LectureVO>()
                    .apply {
                        add(LectureVO("더미 데이터입니다", "수학1", ""))
                        add(LectureVO("이차곡선의 성질이 이해가 안 돼요", "기하", ""))
                    }
            )
        }
    }
}