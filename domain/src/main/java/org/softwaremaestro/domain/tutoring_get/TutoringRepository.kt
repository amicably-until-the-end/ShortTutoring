package org.softwaremaestro.domain.tutoring_get

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.best_teacher_get.entity.TeacherVO
import org.softwaremaestro.domain.tutoring_get.entity.TutoringVO

interface TutoringRepository {
    suspend fun getTutoring(): Flow<BaseResult<List<TutoringVO>, String>>
}
