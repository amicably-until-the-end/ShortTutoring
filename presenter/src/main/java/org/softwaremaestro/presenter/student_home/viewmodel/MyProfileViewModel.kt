package org.softwaremaestro.presenter.student_home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.profile.entity.MyProfileGetResponseVO
import org.softwaremaestro.domain.profile.usecase.MyProfileGetUseCase
import org.softwaremaestro.presenter.util.Util.logError
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(private val myProfileGetUseCase: MyProfileGetUseCase) :
    ViewModel() {

    private val _myProfile: MutableLiveData<MyProfileGetResponseVO> = MutableLiveData()
    val myProfile: LiveData<MyProfileGetResponseVO> get() = _myProfile

    fun getMyProfile() {
        viewModelScope.launch {
            myProfileGetUseCase.execute()
                .catch { exception ->
                    logError(this@MyProfileViewModel::class.java, exception.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> _myProfile.postValue(result.data)
                        is BaseResult.Error -> logError(
                            this@MyProfileViewModel::class.java,
                            result.toString()
                        )
                    }
                }
        }
    }
}