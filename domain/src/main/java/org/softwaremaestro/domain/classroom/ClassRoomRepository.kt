package org.softwaremaestro.domain.classroom

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.classroom.entity.TutoringInfoVO
import org.softwaremaestro.domain.common.BaseResult

interface ClassRoomRepository {
    suspend fun finishClass(tutoringId: String): Flow<BaseResult<String, String>>
    suspend fun getTutoringInfo(tutoringId: String): Flow<BaseResult<TutoringInfoVO, String>>
}