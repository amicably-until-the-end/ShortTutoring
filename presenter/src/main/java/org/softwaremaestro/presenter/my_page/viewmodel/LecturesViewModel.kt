package org.softwaremaestro.presenter.my_page.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.tutoring_get.entity.TutoringVO
import javax.inject.Inject

@HiltViewModel
class LecturesViewModel @Inject constructor() : ViewModel() {

    private val _lectures: MutableLiveData<List<TutoringVO>> = MutableLiveData()
    val lectures: LiveData<List<TutoringVO>> get() = _lectures

    fun getLectures() {
        viewModelScope.launch {
           
        }
    }
}