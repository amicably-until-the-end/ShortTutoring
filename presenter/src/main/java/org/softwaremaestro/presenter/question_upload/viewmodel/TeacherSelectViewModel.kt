package org.softwaremaestro.presenter.question_upload.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_upload.entity.TeacherPickReqVO
import org.softwaremaestro.domain.question_upload.entity.TeacherPickResVO
import org.softwaremaestro.domain.question_upload.entity.TeacherVO
import org.softwaremaestro.domain.question_upload.usecase.TeacherListGetUseCase
import org.softwaremaestro.domain.question_upload.usecase.TeacherPickUseCase
import org.softwaremaestro.presenter.Util.UIState
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject


@HiltViewModel
class TeacherSelectViewModel @Inject constructor(
    private val teacherListGetUseCase: TeacherListGetUseCase,
    private val teacherPickUseCase: TeacherPickUseCase
) :
    ViewModel() {

    private val timer = Timer()
    private var job: Job? = null


    private val _teacherList: MutableLiveData<List<TeacherVO>> = MutableLiveData();
    val teacherList: LiveData<List<TeacherVO>> get() = _teacherList

    private val _errorMsg: MutableLiveData<String> = MutableLiveData();
    val errorMsg: LiveData<String> get() = _errorMsg

    private val _teacherSelectState: MutableLiveData<UIState<TeacherPickResVO>> = MutableLiveData()
    val teacherSelectState: LiveData<UIState<TeacherPickResVO>> get() = _teacherSelectState


    fun pickTeacher(teacherPickReqVO: TeacherPickReqVO) {
        viewModelScope.launch {

            teacherPickUseCase.execute(teacherPickReqVO)
                .onStart {
                    _teacherSelectState.value = UIState.Loading
                }
                .catch { _ ->
                    _teacherSelectState.value = UIState.Failure
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> {
                            _teacherSelectState.postValue(UIState.Success(result.data))
                        }

                        is BaseResult.Error -> {
                            _teacherSelectState.postValue(UIState.Failure)

                        }
                    }
                }
        }
    }

    fun startGetTeacherList(questionId: String) {
        timer.schedule(object : TimerTask() {
            override fun run() {
                viewModelScope.launch(Dispatchers.IO) {
                    getTeacherList(questionId)
                }
            }
        }, 0, 1000) // Execute every one second (1000 milliseconds)
    }

    suspend fun getTeacherList(questionId: String) {
        teacherListGetUseCase.execute(questionId)
            .catch { exception ->
                Log.e("TeacherSelectViewModel", exception.message.toString())
                _errorMsg.postValue(exception.message.toString())
            }
            .collect { result ->
                when (result) {
                    is BaseResult.Success -> {
                        _teacherList.postValue(result.data)
                    }

                    is BaseResult.Error -> _errorMsg.postValue(result.rawResponse)
                }
            }


    }


}

