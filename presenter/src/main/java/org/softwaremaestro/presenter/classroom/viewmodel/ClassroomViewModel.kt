package org.softwaremaestro.presenter.classroom.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herewhite.sdk.Room
import com.herewhite.sdk.domain.Promise
import com.herewhite.sdk.domain.SDKError
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.classroom.usecase.FinishClassUseCase
import org.softwaremaestro.domain.classroom.usecase.GetTutoringInfoUseCase
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.login.entity.UserVO
import org.softwaremaestro.domain.login.usecase.AutoLoginUseCase
import org.softwaremaestro.domain.login.usecase.LoginUseCase
import org.softwaremaestro.domain.login.usecase.SaveKakaoJWTUseCase
import javax.inject.Inject

@HiltViewModel
class ClassroomViewModel @Inject constructor(
    private val finishClassUseCase: FinishClassUseCase,
    private val getTutoringInfoUseCase: GetTutoringInfoUseCase
) :
    ViewModel() {


    private val _finishClassResult = MutableLiveData<String>()
    val finishClassResult: LiveData<String> = _finishClassResult


    fun finishClass(tutoringId: String) {
        viewModelScope.launch {
            finishClassUseCase.execute(tutoringId)
                .onStart {

                }
                .catch { e ->
                    Log.d("error", e.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> {
                            _finishClassResult.postValue("finished")
                        }

                        is BaseResult.Error -> {
                            _finishClassResult.postValue("error")
                        }
                    }
                }
        }
    }


}