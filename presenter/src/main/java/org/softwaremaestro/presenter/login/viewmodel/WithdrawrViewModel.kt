package org.softwaremaestro.presenter.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.login.usecase.WithdrawUseCase
import org.softwaremaestro.presenter.util.UIState
import org.softwaremaestro.presenter.util.Util.logError
import javax.inject.Inject

@HiltViewModel
class WithdrawViewModel @Inject constructor(private val withdrawUseCase: WithdrawUseCase) :
    ViewModel() {

    private val _withdrawState = MutableLiveData<UIState<Boolean>>(UIState.Empty)
    val withdrawState: LiveData<UIState<Boolean>> get() = _withdrawState

    fun withdraw() {
        viewModelScope.launch(Dispatchers.IO) {
            withdrawUseCase.execute()
                .onStart {
                    _withdrawState.postValue(UIState.Loading)
                }
                .catch {
                    _withdrawState.postValue(UIState.Failure)
                    logError(this@WithdrawViewModel::class.java, it.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> _withdrawState.postValue(UIState.Success(result.data))
                        is BaseResult.Error -> {
                            _withdrawState.postValue(UIState.Failure)
                            logError(this@WithdrawViewModel::class.java, result.toString())
                        }
                    }
                }
        }
    }

    fun resetWithdrawState() {
        _withdrawState.postValue(UIState.Empty)
    }
}