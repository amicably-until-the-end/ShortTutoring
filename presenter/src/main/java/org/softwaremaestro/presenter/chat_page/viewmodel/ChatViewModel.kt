package org.softwaremaestro.presenter.chat_page.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.chat.entity.ChatRoomListVO
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.MessageBodyVO
import org.softwaremaestro.domain.chat.entity.MessageVO
import org.softwaremaestro.domain.chat.entity.QuestionState
import org.softwaremaestro.domain.chat.entity.RoomType
import org.softwaremaestro.domain.chat.usecase.GetChatRoomListUseCase
import org.softwaremaestro.domain.classroom.entity.TutoringInfoVO
import org.softwaremaestro.domain.classroom.usecase.GetTutoringInfoUseCase
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.presenter.util.UIState
import java.time.LocalDateTime
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


    fun getChatRoomList() {
        viewModelScope.launch {
            getChatRoomListUseCase.execute()
                .onStart { _reservedNormalChatRoomList.value = UIState.Loading }
                .catch { exception ->
                    _reservedNormalChatRoomList.value = UIState.Failure
                    Log.e(this@ChatViewModel::class.java.name, exception.message.toString())
                }
                .collect { result ->
                    val dummyData = ChatRoomListVO(
                        normalProposed = mutableListOf<ChatRoomVO>().apply {
                            add(getDummyImage(0))
                            repeat(3) {
                                add(getDummyText(it))
                            }
                        },
                        normalReserved = listOf(),
                        selectedProposed = listOf(),
                        selectedReserved = listOf()
                    )
                    when (result) {
                        is BaseResult.Success -> {
                            _reservedNormalChatRoomList.value =
                                UIState.Success(result.data.normalReserved)
                            _reservedSelectedChatRoomList.value =
                                UIState.Success(result.data.selectedReserved)
                            _proposedNormalChatRoomList.value =
                                UIState.Success(dummyData.normalProposed)
                            _proposedSelectedChatRoomList.value =
                                UIState.Success(result.data.selectedProposed)
                        }

                        is BaseResult.Error -> _reservedNormalChatRoomList.value = UIState.Failure
                    }
                }
        }
    }

    private fun getDummyImage(i: Int): ChatRoomVO {
        return ChatRoomVO(
            id = "id",
            roomType = RoomType.TEACHER,
            roomImage = "",
            questionState = QuestionState.PROPOSED,
            questionId = "questionId",
            opponentId = "opponentId",
            title = "타이틀${i}",
            schoolSubject = "미적분",
            schoolLevel = "고등학교",
            messages = listOf(
                MessageVO(
                    time = LocalDateTime.now(),
                    bodyVO = MessageBodyVO.ProblemImage(
                        "https://ibb.co/w7xj4kP",
                        "설명${i}"
                    ),
                    sender = "sender",
                    isMyMsg = false
                )
            ),
            teachers = null,
            isSelect = false
        )
    }

    private fun getDummyText(i: Int): ChatRoomVO {
        return ChatRoomVO(
            id = "id",
            roomType = RoomType.TEACHER,
            roomImage = "",
            questionState = QuestionState.PROPOSED,
            questionId = "questionId",
            opponentId = "opponentId",
            title = "타이틀${i}",
            schoolSubject = "미적분",
            schoolLevel = "고등학교",
            messages = listOf(
                MessageVO(
                    time = LocalDateTime.now(),
                    bodyVO = MessageBodyVO.Text("질문${i}"),
                    sender = "sender",
                    isMyMsg = true
                )
            ),
            teachers = null,
            isSelect = false
        )
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