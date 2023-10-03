package org.softwaremaestro.presenter.teacher_profile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.follow.usecase.UserFollowUseCase
import org.softwaremaestro.domain.follow.usecase.UserUnfollowUseCase
import org.softwaremaestro.presenter.util.Util.logError
import javax.inject.Inject

@HiltViewModel
class FollowUserViewModel @Inject constructor(
    private val userFollowUseCase: UserFollowUseCase,
    private val userUnfollowUseCase: UserUnfollowUseCase
) :
    ViewModel() {

    private val _followUserState = MutableLiveData(false)
    val followUserState: MutableLiveData<Boolean> get() = _followUserState

    fun followUser(userId: String) {
        viewModelScope.launch {
            userFollowUseCase.followUser(userId)
                .catch { exception ->
                    logError(this@FollowUserViewModel::class.java, exception.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> _followUserState.postValue(true)

                        is BaseResult.Error -> logError(
                            this@FollowUserViewModel::class.java,
                            result.toString()
                        )
                    }
                }
        }
    }

    fun unfollowUser(userId: String) {
        viewModelScope.launch {
            userUnfollowUseCase.unfollowUser(userId)
                .catch { exception ->
                    logError(this@FollowUserViewModel::class.java, exception.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> _followUserState.postValue(true)

                        is BaseResult.Error -> logError(
                            this@FollowUserViewModel::class.java,
                            result.toString()
                        )
                    }
                }
        }
    }
}