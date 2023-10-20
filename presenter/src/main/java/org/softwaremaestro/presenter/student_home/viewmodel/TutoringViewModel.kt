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
import org.softwaremaestro.domain.tutoring_get.entity.TutoringVO
import org.softwaremaestro.domain.tutoring_get.usecase.TutoringGetUseCase
import org.softwaremaestro.presenter.util.Util
import javax.inject.Inject

@HiltViewModel
class TutoringViewModel @Inject constructor(private val tutoringGetUseCase: TutoringGetUseCase) :
    ViewModel() {
    private val _tutoring = MutableLiveData<List<TutoringVO>>()
    val tutoring: LiveData<List<TutoringVO>> get() = _tutoring

    fun getTutoring() {
        viewModelScope.launch(Dispatchers.IO) {
            tutoringGetUseCase.execute()
                .catch { exception ->
                    Util.logError(
                        this@TutoringViewModel::class.java,
                        exception.message.toString()
                    )
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> _tutoring.postValue(result.data)
                        is BaseResult.Error -> Util.logError(
                            this@TutoringViewModel::class.java,
                            result.toString()
                        )
                    }
                }
        }
    }
}