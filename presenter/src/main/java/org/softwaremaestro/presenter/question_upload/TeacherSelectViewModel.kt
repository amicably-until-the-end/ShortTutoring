package org.softwaremaestro.presenter.question_upload

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_upload.entity.TeacherVO
import org.softwaremaestro.domain.question_upload.usecase.TeacherListGetUseCase
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject


@HiltViewModel
class TeacherSelectViewModel @Inject constructor(private val teacherListGetUseCase: TeacherListGetUseCase) :
    ViewModel() {

    private val timer = Timer()
    private var job: Job? = null


    private val _teacherList: MutableLiveData<List<TeacherVO>> = MutableLiveData();
    val teacherList: LiveData<List<TeacherVO>> get() = _teacherList

    private val _errorMsg: MutableLiveData<String> = MutableLiveData();
    val errorMsg: LiveData<String> get() = _errorMsg

    fun startGetTeacherList(questionId: String) {
        timer.schedule(object : TimerTask() {
            override fun run() {
                viewModelScope.launch(Dispatchers.IO) {
                    Log.d("mymymy", "thread is working")
                    getTeacherList(questionId)
                }
            }
        }, 0, 1000) // Execute every one second (1000 milliseconds)
    }

    suspend fun getTeacherList(questionId: String) {
        teacherListGetUseCase.execute(questionId)
            .catch { exception ->
                Log.d("mymymy", exception.toString())
                _errorMsg.postValue(exception.message.toString())
            }
            .collect { result ->
                Log.d("mymymy", result.toString())
                when (result) {
                    is BaseResult.Success -> {
                        _teacherList.postValue(result.data)
                    }

                    is BaseResult.Error -> _errorMsg.postValue(result.rawResponse)
                }
            }


    }


}

