package org.softwaremaestro.presenter.student_home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.tutoring_get.entity.TutoringVO
import javax.inject.Inject

@HiltViewModel
class LectureViewModel @Inject constructor() : ViewModel() {
    private val _lectures = MutableLiveData<List<TutoringVO>>()
    val lectures: LiveData<List<TutoringVO>> get() = _lectures

    fun getLectures() {
        viewModelScope.launch {
        }
    }
}