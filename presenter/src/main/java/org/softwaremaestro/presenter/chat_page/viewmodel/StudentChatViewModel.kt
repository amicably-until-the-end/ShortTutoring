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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.MessageVO
import org.softwaremaestro.domain.chat.entity.QuestionState
import org.softwaremaestro.domain.chat.entity.QuestionType
import org.softwaremaestro.domain.chat.usecase.GetChatRoomListUseCase
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_upload.usecase.TeacherPickUseCase
import org.softwaremaestro.presenter.util.UIState
import javax.inject.Inject

@HiltViewModel
class StudentChatViewModel @Inject constructor(
    private val teacherPickUseCase: TeacherPickUseCase
) :
    ViewModel() {

    private var socket: Socket? = null

    private val pickTeacherResult = MutableLiveData<UIState<Boolean>>()
    val pickTeacherResultState: LiveData<UIState<Boolean>> get() = pickTeacherResult


    fun pickTeacher(chattingId: String, questionId: String) {
        viewModelScope.launch {
            teacherPickUseCase.execute(chattingId, questionId)
                .onStart { pickTeacherResult.value = UIState.Loading }
                .catch { exception ->
                    pickTeacherResult.value = UIState.Failure
                    Log.e(this@StudentChatViewModel::class.java.name, exception.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> {
                            pickTeacherResult.value = UIState.Success(true)
                        }

                        is BaseResult.Error -> {
                            pickTeacherResult.value = UIState.Failure
                        }
                    }
                }
        }
    }

}