package org.softwaremaestro.presenter.chat_page.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.MessageVO
import org.softwaremaestro.domain.chat.entity.QuestionState
import org.softwaremaestro.domain.chat.entity.QuestionType
import org.softwaremaestro.domain.chat.usecase.GetChatRoomListUseCase
import org.softwaremaestro.domain.classroom.entity.TutoringInfoVO
import org.softwaremaestro.domain.classroom.usecase.GetTutoringInfoUseCase
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.presenter.util.UIState
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatRoomListUseCase: GetChatRoomListUseCase,
    private val getTutoringInfoUseCase: GetTutoringInfoUseCase,
) :
    ViewModel() {

    private var socket: Socket? = null


    private val _reservedNormalChatRoomList = MutableLiveData<UIState<List<ChatRoomVO>>>()
    val reservedNormalChatRoomList: LiveData<UIState<List<ChatRoomVO>>>
        get() = _reservedNormalChatRoomList

    private val _reservedSelectedChatRoomList = MutableLiveData<UIState<List<ChatRoomVO>>>()
    val reservedSelectedChatRoomList: LiveData<UIState<List<ChatRoomVO>>>
        get() = _reservedSelectedChatRoomList

    private val _proposedNormalChatRoomList = MutableLiveData<UIState<List<ChatRoomVO>>>()
    val proposedNormalChatRoomList: LiveData<UIState<List<ChatRoomVO>>>
        get() = _proposedNormalChatRoomList

    private val _proposedSelectedChatRoomList = MutableLiveData<UIState<List<ChatRoomVO>>>()
    val proposedSelectedChatRoomList: LiveData<UIState<List<ChatRoomVO>>>
        get() = _proposedSelectedChatRoomList

    val _tutoringInfo = MutableLiveData<UIState<TutoringInfoVO>>()
    val tutoringInfo: LiveData<UIState<TutoringInfoVO>>
        get() = _tutoringInfo


    fun getChatRoomList(isTeacher: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            getChatRoomListUseCase.execute(isTeacher)
                .onStart { _reservedNormalChatRoomList.postValue(UIState.Loading) }
                .catch { exception ->
                    _reservedNormalChatRoomList.postValue(UIState.Failure)
                    Log.e(this@ChatViewModel::class.java.name, exception.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> {
                            _reservedNormalChatRoomList.postValue(
                                UIState.Success(result.data.normalReserved)
                            )
                            _reservedSelectedChatRoomList.postValue(
                                UIState.Success(result.data.selectedReserved)
                            )
                            _proposedNormalChatRoomList.postValue(
                                UIState.Success(result.data.normalProposed)
                            )
                            _proposedSelectedChatRoomList.postValue(
                                UIState.Success(result.data.selectedProposed)
                            )
                        }

                        is BaseResult.Error -> _reservedNormalChatRoomList.postValue(UIState.Failure)
                    }
                }
        }
    }

    fun getClassRoomInfo(questionId: String) {
        viewModelScope.launch {
            getTutoringInfoUseCase.execute(questionId)
                .onStart {
                    _tutoringInfo.value = UIState.Loading
                }
                .catch { exception ->
                    _tutoringInfo.value = UIState.Failure
                    Log.e(this@ChatViewModel::class.java.name, exception.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> {
                            _tutoringInfo.value = UIState.Success(result.data)
                        }

                        is BaseResult.Error -> UIState.Failure
                    }
                }
        }

    }

    private fun initSocket(questionId: String) {
        try {
            socket = IO.socket("http://10.0.2.2:3000")
            Log.d("socket", "init socket ${socket?.id()}")
            socket?.connect()
            socket?.apply {
                on(Socket.EVENT_CONNECT) {
                    Log.d("socket", "connect")
                    emit("join", questionId)
                }
                on("msg") { args ->
                    val msg = args[0] as String
                    Log.d("socket", "receive message ${msg}")
                }
            }
        } catch (e: Exception) {
            Log.d("socket", e.message.toString())
        }
    }

    fun sendMessage(string: String) {
        socket?.emit("msg", string)
        Log.d("socket", "send message")
    }

    override fun onCleared() {
        super.onCleared()
        socket?.disconnect()
    }

}