package org.softwaremaestro.presenter.teacher_my_page.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.follow.entity.FollowerGetResponseVO
import org.softwaremaestro.domain.follow.usecase.FollowerGetUseCase
import org.softwaremaestro.presenter.util.Util.logError
import javax.inject.Inject

@HiltViewModel
class FollowerViewModel @Inject constructor(private val followerGetUseCase: FollowerGetUseCase) :
    ViewModel() {

    private val _follower = MutableLiveData<List<FollowerGetResponseVO>>()
    val follower: LiveData<List<FollowerGetResponseVO>> get() = _follower

    fun getFollower(teacherId: String) {
        viewModelScope.launch {
            followerGetUseCase.execute(teacherId)
                .catch { exception ->
                    logError(this@FollowerViewModel::class.java, exception.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> {
                            _follower.postValue(result.data)
                        }

                        is BaseResult.Error -> logError(
                            this@FollowerViewModel::class.java,
                            result.toString()
                        )
                    }
                }
        }
    }
}