package org.softwaremaestro.presenter.student_home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.profile_get.entity.ProfileGetResponseVO
import org.softwaremaestro.domain.profile_get.usecase.ProfileGetUseCase
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileGetUseCase: ProfileGetUseCase) :
    ViewModel() {

    private val _profile: MutableLiveData<ProfileGetResponseVO> = MutableLiveData()
    val profile: LiveData<ProfileGetResponseVO> get() = _profile

    fun getProfile(userId: String) {
        viewModelScope.launch {
            profileGetUseCase.execute(userId)
                .catch { exception ->
                    // Todo: 추후에 에러 어떻게 처리할지 생각해보기
                    Log.d("Error", exception.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> _profile.postValue(result.data)
                        is BaseResult.Error -> Log.d("Error", result.toString())
                    }
                }
        }
    }
}