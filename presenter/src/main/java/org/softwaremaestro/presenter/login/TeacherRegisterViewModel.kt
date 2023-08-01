package org.softwaremaestro.presenter.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.login.entity.TeacherRegisterVO
import org.softwaremaestro.domain.login.usecase.TeacherRegisterUseCase
import javax.inject.Inject

@HiltViewModel
class TeacherRegisterViewModel @Inject constructor(
    private val teacherRegisterUseCase: TeacherRegisterUseCase
) :
    ViewModel() {

    val _admissonYear: MutableLiveData<String> = MutableLiveData()
    val _college: MutableLiveData<String> = MutableLiveData()
    val _major: MutableLiveData<String> = MutableLiveData()
    val _univ: MutableLiveData<String> = MutableLiveData()

    val admissonYear: LiveData<String> get() = _admissonYear
    val college: LiveData<String> get() = _college
    val major: LiveData<String> get() = _major
    val univ: LiveData<String> get() = _univ

    private val _registerResult: MutableLiveData<Boolean> = MutableLiveData()
    val registerResult: LiveData<Boolean> get() = _registerResult

    fun registerTeacher() {
        viewModelScope.launch {
            teacherRegisterUseCase.execute(
                TeacherRegisterVO(
                    admissonYear.value,
                    college.value,
                    univ.value,
                    major.value
                )
            ).catch { exception ->
                _registerResult.postValue(false)
            }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> {
                            _registerResult.postValue(true)
                        }

                        is BaseResult.Error -> {
                            _registerResult.postValue(false)
                        }
                    }
                }

        }
    }


}