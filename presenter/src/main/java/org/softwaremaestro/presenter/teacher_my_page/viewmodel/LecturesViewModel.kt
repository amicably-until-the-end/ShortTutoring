package org.softwaremaestro.presenter.teacher_my_page.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.review_get.LectureVO
import javax.inject.Inject

@HiltViewModel
class LecturesViewModel @Inject constructor() : ViewModel() {

    private val _lectures: MutableLiveData<List<LectureVO>> = MutableLiveData()
    val lectures: LiveData<List<LectureVO>> get() = _lectures

    fun getLectures() {
        viewModelScope.launch {
            val mockUpLectures = mutableListOf<LectureVO>().apply {
                (0..5).forEach {
                    add(
                        LectureVO(
                            "문제가 너무 어려워요",
                            "수학1",
                            "example url"
                        )
                    )
                }
            }

            _lectures.postValue(mockUpLectures.toList())
        }
    }
}