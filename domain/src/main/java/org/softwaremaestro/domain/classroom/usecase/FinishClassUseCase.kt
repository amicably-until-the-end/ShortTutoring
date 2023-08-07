package org.softwaremaestro.domain.classroom.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.classroom.ClassRoomRepository
import org.softwaremaestro.domain.classroom.entity.TutoringInfoVO
import org.softwaremaestro.domain.common.BaseResult
import javax.inject.Inject

class FinishClassUseCase @Inject constructor(private val repository: ClassRoomRepository) {
    suspend fun execute(tutoringId: String): Flow<BaseResult<TutoringInfoVO, String>> =
        repository.getTutoringInfo(tutoringId)
}