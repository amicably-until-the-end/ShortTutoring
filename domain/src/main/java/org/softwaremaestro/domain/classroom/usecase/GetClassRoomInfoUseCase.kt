package org.softwaremaestro.domain.classroom.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.classroom.ClassRoomRepository
import org.softwaremaestro.domain.classroom.entity.ClassroomInfoVO
import org.softwaremaestro.domain.common.BaseResult
import javax.inject.Inject

class GetClassRoomInfoUseCase @Inject constructor(private val repository: ClassRoomRepository) {
    suspend fun execute(questionId: String): Flow<BaseResult<ClassroomInfoVO, String>> =
        repository.getClassroomInfo(questionId)
}