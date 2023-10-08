package org.softwaremaestro.presenter.coin.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.coin.usecase.CoinFreeReceiveUseCase
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.presenter.util.Util.logError
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(private val coinFreeReceive: CoinFreeReceiveUseCase) :
    ViewModel() {

    private val _coinFreeReceiveState: MutableLiveData<Boolean> = MutableLiveData()
    val coinFreeReceiveState: LiveData<Boolean> get() = _coinFreeReceiveState

    fun receiveCoinFree() {
        viewModelScope.launch {
            coinFreeReceive.execute()
                .catch { exception ->
                    logError(this@CoinViewModel::class.java, exception.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> result.data.let {
                            _coinFreeReceiveState.postValue(true)
                        }

                        is BaseResult.Error -> {
                            _coinFreeReceiveState.postValue(false)
                            logError(
                                this@CoinViewModel::class.java,
                                result.toString()
                            )
                        }
                    }
                }
        }
    }
}