package org.softwaremaestro.presenter.teacher_search.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.profile.entity.MyProfileResVO
import org.softwaremaestro.domain.profile.usecase.MyProfileGetUseCase
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(private val myProfileGetUseCase: MyProfileGetUseCase) :
    ViewModel() {

    private val _myProfile: MutableLiveData<MyProfileResVO> = MutableLiveData()
    val myProfile: LiveData<MyProfileResVO> get() = _myProfile

    fun getMyProfile() {
        viewModelScope.launch {
            myProfileGetUseCase.execute()
                .catch { exception ->
                    // Todo: 추후에 에러 어떻게 처리할지 생각해보기
                    Log.d("Error", exception.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> _myProfile.postValue(result.data)
                        is BaseResult.Error -> Log.d("Error", result.toString())
                    }
                }
        }
    }
}