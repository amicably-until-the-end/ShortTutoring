package org.softwaremaestro.presenter

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.softwaremaestro.domain.usecase.GetRequestsUseCase
import javax.inject.Inject

class TeacherHomeViewModel constructor(
    private val getRequestsUseCase: GetRequestsUseCase
): ViewModel() {
    // Todo: ViewModel 구현은 추후에 관련 내용을 학습하고 진행할 예정
}