package org.softwaremaestro.presenter.teacher_profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.best_teacher_get.entity.TeacherVO
import org.softwaremaestro.domain.best_teacher_get.usecase.BestTeacherGetUseCase
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.presenter.util.Util
import javax.inject.Inject

@HiltViewModel
class BestTeacherViewModel @Inject constructor(private val bestTeacherGetUseCase: BestTeacherGetUseCase) :
    ViewModel() {
    private val _bestTeachers: MutableLiveData<List<TeacherVO>> = MutableLiveData()
    val bestTeachers: LiveData<List<TeacherVO>> get() = _bestTeachers

    fun getTeachers() {
        viewModelScope.launch(Dispatchers.IO) {
            bestTeacherGetUseCase.execute()
                .catch { exception ->
                    Util.logError(
                        this@BestTeacherViewModel::class.java,
                        exception.message.toString()
                    )
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> _bestTeachers.postValue(result.data)
                        is BaseResult.Error -> Util.logError(
                            this@BestTeacherViewModel::class.java,
                            result.toString()
                        )
                    }
                }
        }
    }

}