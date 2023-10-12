package org.softwaremaestro.presenter.student_home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.event.entity.EventsVO
import org.softwaremaestro.domain.event.usecase.EventsGetUseCase
import org.softwaremaestro.presenter.util.Util.logError
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(private val eventsGetUseCase: EventsGetUseCase) :
    ViewModel() {

    private val _events = MutableLiveData<EventsVO>()
    val events: LiveData<EventsVO> get() = _events

    fun getEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            eventsGetUseCase.execute()
                .catch { exception ->
                    logError(
                        this@EventViewModel::class.java,
                        exception.message.toString()
                    )
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> _events.postValue(result.data)
                        is BaseResult.Error -> logError(
                            this@EventViewModel::class.java,
                            result.toString()
                        )
                    }
                }
        }
    }
}