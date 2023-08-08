package org.softwaremaestro.presenter.teacher_my_page.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class NumOfFollowerViewModel @Inject constructor() : ViewModel() {

    private val _numOfFollower: MutableLiveData<Int> = MutableLiveData()
    val numOfFollower: LiveData<Int> get() = _numOfFollower

    fun getNumOfFollower() {
        viewModelScope.launch {
            val mockUpNum = 35
            _numOfFollower.value = mockUpNum
        }
    }

    fun addOne() = _numOfFollower.postValue(_numOfFollower.value!! + 1)

    fun minusOne() = _numOfFollower.postValue(_numOfFollower.value!! - 1)
}