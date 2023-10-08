package org.softwaremaestro.presenter.student_home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.chat.usecase.GetChatRoomListUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getChatRoomListUseCase: GetChatRoomListUseCase
) : ViewModel() {
    var chattingId: String? = null

    private val _newMessageExist: MutableLiveData<Boolean> = MutableLiveData(false)
    val newMessageExist: MutableLiveData<Boolean> get() = _newMessageExist

    fun getNewMessageExist() {
        viewModelScope.launch(Dispatchers.IO) {
            _newMessageExist.postValue(getChatRoomListUseCase.hasUnreadMessages())

        }
    }

}