package org.softwaremaestro.domain.classroom.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.classroom.ClassRoomRepository
import org.softwaremaestro.domain.classroom.entity.TutoringInfoVO
import org.softwaremaestro.domain.common.BaseResult
import javax.inject.Inject

class GetTutoringInfoUseCase @Inject constructor(private val repository: ClassRoomRepository) {
    suspend fun execute(questionId: String): Flow<BaseResult<TutoringInfoVO, String>> =
        repository.getTutoringInfo(questionId)
}