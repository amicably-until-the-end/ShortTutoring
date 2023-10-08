package org.softwaremaestro.presenter.my_page.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.follow.entity.FollowingGetResponseVO
import org.softwaremaestro.domain.follow.usecase.FollowingGetUseCase
import org.softwaremaestro.presenter.util.Util.logError
import javax.inject.Inject

@HiltViewModel
class FollowingViewModel @Inject constructor(private val followingGetUseCase: FollowingGetUseCase) :
    ViewModel() {

    private val _following = MutableLiveData<List<FollowingGetResponseVO>>()
    val following: LiveData<List<FollowingGetResponseVO>> get() = _following

    fun getFollowing(userId: String) {
        viewModelScope.launch {
            followingGetUseCase.execute(userId)
                .catch { exception ->
                    logError(this@FollowingViewModel::class.java, exception.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> {
                            _following.postValue(result.data)
                        }

                        is BaseResult.Error -> logError(
                            this@FollowingViewModel::class.java,
                            result.toString()
                        )
                    }
                }
        }
    }
}