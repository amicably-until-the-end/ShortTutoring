package org.softwaremaestro.presenter.student_home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.best_teacher_get.entity.TeacherVO
import org.softwaremaestro.domain.teacher_onlines_get.usecase.TeacherOnlinesGetUseCase
import org.softwaremaestro.presenter.util.Util
import javax.inject.Inject

@HiltViewModel
class TeacherOnlineViewModel @Inject constructor(private val teacherOnlinesGetUseCase: TeacherOnlinesGetUseCase) :
    ViewModel() {

    private val _teacherOnlines: MutableLiveData<List<TeacherVO>> = MutableLiveData()
    val teacherOnlines: LiveData<List<TeacherVO>> get() = _teacherOnlines

    fun getTeacherOnlines() {
        viewModelScope.launch {
            teacherOnlinesGetUseCase.execute()
                .catch { exception ->
                    Util.logError(
                        this@TeacherOnlineViewModel::class.java,
                        exception.message.toString()
                    )
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> _teacherOnlines.postValue(result.data)
                        is BaseResult.Error -> Util.logError(
                            this@TeacherOnlineViewModel::class.java,
                            result.toString()
                        )
                    }
                }
        }
    }
}