package org.softwaremaestro.presenter.login.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.univcert.api.UnivCert
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.login.entity.TeacherRegisterVO
import org.softwaremaestro.domain.login.usecase.TeacherRegisterUseCase
import org.softwaremaestro.presenter.BuildConfig
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


    private val _sendEmailResult: MutableLiveData<Boolean> = MutableLiveData()
    val sendEmailResult: LiveData<Boolean> get() = _sendEmailResult

    private val _checkEmailResult: MutableLiveData<Boolean> = MutableLiveData()
    val checkEmailResult: LiveData<Boolean> get() = _checkEmailResult

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

    fun sendVerificationMail(emailAddress: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val sendResult = UnivCert.certify(
                BuildConfig.UNIVCERT_API_KEY,
                emailAddress,
                univ.value,
                false,
            )
            Log.d("email", sendResult.toString())
            if (sendResult["success"] == true) {
                _sendEmailResult.postValue(true)
            } else {
                _sendEmailResult.postValue(false)
            }
        }
    }

    fun checkEmailCode(email: String, code: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val checkResult = UnivCert.certifyCode(
                BuildConfig.UNIVCERT_API_KEY,
                email,
                univ.value,
                code
            )
            if (checkResult["success"] == true) {
                _checkEmailResult.postValue(true)
            } else {
                _checkEmailResult.postValue(false)
            }
        }
    }


}