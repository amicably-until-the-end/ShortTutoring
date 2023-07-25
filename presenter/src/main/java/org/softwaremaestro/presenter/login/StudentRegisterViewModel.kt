package org.softwaremaestro.presenter.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.login.entity.StudentRegisterVO
import org.softwaremaestro.domain.login.usecase.StudentRegisterUseCase
import javax.inject.Inject

@HiltViewModel
class StudentRegisterViewModel @Inject constructor(
    private val studentRegisterUseCase: StudentRegisterUseCase
) :
    ViewModel() {

    var grade: Int = 1 //1: 1학년, 2:2학년, 3:3학년
    var school: Int = 0 //0: 중학교 , 1 : 고등학교

    private val _registerSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val registerSuccess: MutableLiveData<Boolean> get() = _registerSuccess

    fun registerStudent() {
        viewModelScope.launch {

            studentRegisterUseCase.execute(StudentRegisterVO(school, grade))
                .catch { exception ->
                    _registerSuccess.postValue(false)
                    Log.e("mymymy", "register fail ${exception.toString()}")
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> {
                            Log.d("mymymy", "success register ${result.data}")
                            _registerSuccess.postValue(true)
                        }

                        is BaseResult.Error -> {
                            _registerSuccess.postValue(false)
                        }
                    }
                }
        }
    }

}