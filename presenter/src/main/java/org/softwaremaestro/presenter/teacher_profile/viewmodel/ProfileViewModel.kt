package org.softwaremaestro.presenter.teacher_profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.profile.entity.ProfileGetResponseVO
import org.softwaremaestro.domain.profile.usecase.ProfileGetUseCase
import org.softwaremaestro.presenter.util.Util.logError
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
                    logError(this@ProfileViewModel::class.java, exception.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> {
                            _profile.postValue(result.data)
                        }

                        is BaseResult.Error -> logError(
                            this@ProfileViewModel::class.java,
                            result.toString()
                        )
                    }
                }
        }
    }
}