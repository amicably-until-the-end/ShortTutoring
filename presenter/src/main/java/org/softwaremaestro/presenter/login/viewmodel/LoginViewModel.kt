package org.softwaremaestro.presenter.login.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.login.entity.UserVO
import org.softwaremaestro.domain.login.usecase.AutoLoginUseCase
import org.softwaremaestro.domain.login.usecase.LoginUseCase
import org.softwaremaestro.domain.login.usecase.SaveKakaoJWTUseCase
import org.softwaremaestro.presenter.util.UIState
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val autoLoginUseCase: AutoLoginUseCase,
    private val loginUseCase: LoginUseCase,
    private val saveKakaoJWTUseCase: SaveKakaoJWTUseCase
) :
    ViewModel() {

    private val _saveRole: MutableLiveData<UIState<String>> = MutableLiveData()
    val saveRole: LiveData<UIState<String>> get() = _saveRole

    private val _userRole: MutableLiveData<String> = MutableLiveData()
    val userRole: LiveData<String> get() = _userRole

    private val _errorMsg: MutableLiveData<String> = MutableLiveData()
    val errorMsg: LiveData<String> get() = _errorMsg

    private val _isKakaoLoginSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isKakaoLoginSuccess: LiveData<Boolean> get() = _isKakaoLoginSuccess // kakao login 성공하면 true


    /**
     * 카카오 로그인
     */

    fun clearUserRole() {
        _userRole.postValue("")
    }


    fun loginWithKakao(context: Context) {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e("kakao", "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(
                        context
                    ) { _, accountError ->
                        if (accountError != null) {
                            Log.e("kakao", "카카오계정으로 로그인 실패 ${accountError}")
                        } else {
                            if (token?.idToken != null) {
                                login()
                            }
                        }
                    }
                } else if (token != null) {
                    Log.i("kakao", "카카오톡으로 로그인 성공 ${token.idToken}")
                    login()

                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                if (error != null) {
                    Log.e("kakao", "카카오계정으로 로그인 실패", error)
                } else {
                    login()
                }
            }
        }
    }

    fun getSavedToken() {
        viewModelScope.launch {
            autoLoginUseCase.execute()
                .catch {
                    _saveRole.postValue(UIState.Failure)
                }
                .onStart { _saveRole.postValue(UIState.Loading) }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> _saveRole.postValue(UIState.Success(result.data))
                        else -> _saveRole.postValue(UIState.Failure)
                    }
                }
        }
    }

    fun login() {
        viewModelScope.launch {
            loginUseCase.execute()
                .catch {
                    Log.e("login", "login fail", it)
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> {
                            //TODO: enum으로 바꾸기
                            _userRole.postValue(result.data)
                        }

                        else -> {
                            _userRole.postValue("not registered")
                        }
                    }

                }
        }
    }


}