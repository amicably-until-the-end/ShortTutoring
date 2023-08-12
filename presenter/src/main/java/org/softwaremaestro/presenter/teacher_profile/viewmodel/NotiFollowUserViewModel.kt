package org.softwaremaestro.presenter.teacher_profile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.user_follow.usecase.UserFollowUseCase
import javax.inject.Inject

@HiltViewModel
class NotiFollowUserViewModel @Inject constructor(private val userFollowUseCase: UserFollowUseCase) :
    ViewModel() {

    private val _notiFollowUser: MutableLiveData<String> = MutableLiveData()
    val notiFollowUser: LiveData<String> get() = _notiFollowUser

    private val _notiUnfollowUser: MutableLiveData<String> = MutableLiveData()
    val notiUnfollowUser: LiveData<String> get() = _notiFollowUser

    fun followUser(userId: String) {
        viewModelScope.launch {
            userFollowUseCase.followUser(userId)
                .catch { exception ->
                    // Todo: 추후에 에러 어떻게 처리할지 생각해보기
                    Log.d("Error", exception.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> _notiFollowUser.postValue(result.data)
                        is BaseResult.Error -> Log.d("Error", result.toString())
                    }
                }
        }
    }

    fun unfollowUser(userId: String) {
        viewModelScope.launch {
            userFollowUseCase.unfollowUser(userId)
                .catch { exception ->
                    // Todo: 추후에 에러 어떻게 처리할지 생각해보기
                    Log.d("Error", exception.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> _notiUnfollowUser.postValue(result.data)
                        is BaseResult.Error -> Log.d("Error", result.toString())
                    }
                }
        }
    }
}