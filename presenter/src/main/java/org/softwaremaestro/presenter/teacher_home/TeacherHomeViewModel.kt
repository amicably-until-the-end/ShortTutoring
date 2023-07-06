package org.softwaremaestro.presenter.teacher_home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.softwaremaestro.domain.usecase.GetRequestsUseCase
import javax.inject.Inject

@HiltViewModel
class TeacherHomeViewModel @Inject constructor(
    private val getRequestsUseCase: GetRequestsUseCase
): ViewModel() {
    // Todo: ViewModel 구현은 추후에 관련 내용을 학습하고 진행할 예정
}