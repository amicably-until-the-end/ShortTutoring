package org.softwaremaestro.presenter.teacher_home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.offer_remove.usecase.OfferRemoveUseCase
import javax.inject.Inject

@HiltViewModel
class OfferRemoveViewModel @Inject constructor(private val offerRemoveUseCase: OfferRemoveUseCase) :
    ViewModel() {

    private val _notiOfferRemove: MutableLiveData<String> = MutableLiveData()
    val notiOfferRemove: LiveData<String>
        get() = _notiOfferRemove

    fun removeOffer(questionId: String) {
        viewModelScope.launch {
            offerRemoveUseCase.execute(questionId)
                .catch { exception ->
                    // Todo: 추후에 에러 어떻게 처리할지 생각해보기
                    Log.d("Error", exception.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> _notiOfferRemove.value = result.data
                        is BaseResult.Error -> Log.d("Error", result.toString())
                    }
                }
        }
    }
}