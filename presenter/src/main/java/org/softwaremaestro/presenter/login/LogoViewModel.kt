package org.softwaremaestro.presenter.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.login.entity.UserVO
import org.softwaremaestro.domain.login.usecase.AutoLoginUseCase
import org.softwaremaestro.domain.login.usecase.GetUserInfoUseCase
import javax.inject.Inject

@HiltViewModel
class LogoViewModel @Inject constructor(
    private val autoLoginUseCase: AutoLoginUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase
) :
    ViewModel() {

    private val _savedToken: MutableLiveData<String> = MutableLiveData()
    val savedToken: LiveData<String> get() = _savedToken

    private val _userInfo: MutableLiveData<UserVO> = MutableLiveData()
    val userInfo: LiveData<UserVO> get() = _userInfo

    private val _errorMsg: MutableLiveData<String> = MutableLiveData()
    val errorMsg: LiveData<String> get() = _errorMsg


    fun getSaveToken() {
        viewModelScope.launch {
            autoLoginUseCase.execute()
                .catch {
                    //Auto Login Fail
                    Log.d("mymymy", "Auto login Fail")
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> _savedToken.postValue(result.data)
                        else -> {}
                    }
                }
        }
    }

    fun getUserInfo() {
        viewModelScope.launch {
            getUserInfoUseCase.execute()
                .catch {
                    Log.d("mymymy", "Get User info fail")
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> {
                            _userInfo.postValue(result.data)
                        }

                        else -> {
                            _errorMsg.postValue("로그인 실패")
                        }
                    }

                }
        }
    }

}