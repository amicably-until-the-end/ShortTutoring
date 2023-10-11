package org.softwaremaestro.presenter.chat_page.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.json.JSONObject
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.MessageVO
import org.softwaremaestro.domain.chat.usecase.GetChatMessagesUseCase
import org.softwaremaestro.domain.chat.usecase.GetChatRoomListUseCase
import org.softwaremaestro.domain.chat.usecase.InsertMessageUseCase
import org.softwaremaestro.domain.classroom.entity.ClassroomInfoVO
import org.softwaremaestro.domain.classroom.entity.TutoringInfoVO
import org.softwaremaestro.domain.classroom.usecase.GetClassRoomInfoUseCase
import org.softwaremaestro.domain.classroom.usecase.GetTutoringInfoUseCase
import org.softwaremaestro.domain.classroom.usecase.StartClassUseCase
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.socket.SocketManager
import org.softwaremaestro.presenter.util.UIState
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatRoomListUseCase: GetChatRoomListUseCase,
    private val getTutoringInfoUseCase: GetTutoringInfoUseCase,
    private val getChatMessagesUseCase: GetChatMessagesUseCase,
    private val insertMessageUseCase: InsertMessageUseCase,
    private val getClassRoomInfoUseCase: GetClassRoomInfoUseCase,
    private val startClassUseCase: StartClassUseCase
) :
    ViewModel() {

    @Inject
    lateinit var socketManager: SocketManager


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

    val _classroomInfo = MutableLiveData<UIState<ClassroomInfoVO?>>()
    val classroomInfo: LiveData<UIState<ClassroomInfoVO?>>
        get() = _classroomInfo


    private val _messages = MutableLiveData<UIState<List<MessageVO>>>()
    val messages: LiveData<UIState<List<MessageVO>>>
        get() = _messages

    var selectedQuestionRoom: String? = null

    val _currentChattingRoomVO = MutableLiveData<ChatRoomVO?>()
    val currentChattingRoomVO: LiveData<ChatRoomVO?>
        get() = _currentChattingRoomVO

    val _changedChatRoom = MutableLiveData<ChatRoomVO?>()
    val changedChatRoom: LiveData<ChatRoomVO?>
        get() = _changedChatRoom


    val _tutoringInfo = MutableLiveData<UIState<TutoringInfoVO>>()
    val tutoringInfo: LiveData<UIState<TutoringInfoVO>>
        get() = _tutoringInfo

    val gson = Gson()

    fun getChatRoomList(isTeacher: Boolean, currentRoomId: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            getChatRoomListUseCase.execute(isTeacher, currentRoomId)
                .onStart { _reservedNormalChatRoomList.postValue(UIState.Loading) }
                .catch { exception ->
                    _reservedNormalChatRoomList.postValue(UIState.Failure)
                    Log.e(this@ChatViewModel::class.java.name, exception.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> {
                            result.data.currentRoomVO?.let { _currentChattingRoomVO.postValue(it) }
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

    fun getChatRoomInfo(chattingId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getChatRoomListUseCase.getChatRoom(chattingId).catch { }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> {
                            _changedChatRoom.postValue(result.data)
                        }

                        else -> {}
                    }
                }
        }
    }

    fun getTutoringInfo(tutoringId: String) {
        viewModelScope.launch {
            getTutoringInfoUseCase.execute(tutoringId)
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

                        is BaseResult.Error -> _tutoringInfo.value = UIState.Failure
                    }
                }
        }

    }

    fun startClassroom(tutoringId: String) {
        viewModelScope.launch {
            startClassUseCase.execute(tutoringId)
                .onStart {
                    _classroomInfo.value = UIState.Loading
                }
                .catch { exception ->
                    Log.e(this@ChatViewModel::class.java.name, exception.message.toString())
                    _classroomInfo.value = UIState.Failure
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> {
                            _classroomInfo.value = UIState.Success(result.data)
                        }

                        is BaseResult.Error -> {
                            _classroomInfo.value = UIState.Failure
                        }
                    }
                }
        }
    }

    fun getClassroomInfo(questionId: String) {
        viewModelScope.launch {
            getClassRoomInfoUseCase.execute(questionId)
                .onStart {
                    _classroomInfo.value = UIState.Loading
                }
                .catch { exception ->
                    _classroomInfo.value = UIState.Failure
                    Log.e(this@ChatViewModel::class.java.name, exception.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> {
                            _classroomInfo.value = UIState.Success(result.data)
                        }

                        is BaseResult.Error -> when (result.rawResponse) {
                            ClassroomInfoVO.NOT_YET_START -> _classroomInfo.value =
                                UIState.Success(null)

                            else -> _classroomInfo.value = UIState.Failure
                        }
                    }
                }
        }
    }

    fun getMessages(chattingId: String) {
        viewModelScope.launch(Dispatchers.IO) {

            getChatMessagesUseCase.execute(chattingId)
                .onStart {
                    _messages.postValue(UIState.Loading)
                }
                .catch { exception ->
                    _messages.postValue(UIState.Failure)
                    Log.e(this@ChatViewModel::class.java.name, exception.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> {
                            _messages.postValue(UIState.Success(result.data))
                        }

                        is BaseResult.Error -> _messages.postValue(UIState.Failure)
                    }
                }
        }
    }

    fun sendMessage(body: String, receiverId: String, chattingId: String) {
        var jsonBody = gson.toJson(MessageBody(body))
        var payload = JSONObject(gson.toJson(Message("text", jsonBody, receiverId, chattingId)))
        socketManager.getSocket().emit("message", payload)
        viewModelScope.launch(Dispatchers.IO) {
            insertMessageUseCase.execute(
                chattingId,
                gson.toJson(MessageBody(body)),
                "text",
                LocalDateTime.now().toString(),
                true,
                true
            ).collect()
            getMessages(chattingId)
        }
    }

    fun setMessageUI(state: UIState<List<MessageVO>>) {
        _messages.value = UIState.Empty
    }

    fun markAsRead(chatRoomId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getChatRoomListUseCase.markAsRead(chatRoomId)
        }
    }


    @Serializable
    data class Message(
        val format: String,
        val body: String,
        val receiverId: String,
        val chattingId: String
    )

    @Serializable
    data class MessageBody(
        val text: String
    )

}