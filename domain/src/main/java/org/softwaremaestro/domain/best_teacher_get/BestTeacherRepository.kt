package org.softwaremaestro.domain.best_teacher_get

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.best_teacher_get.entity.TeacherVO

interface BestTeacherRepository {
    suspend fun getBestTeacher(): Flow<BaseResult<List<TeacherVO>, String>>
}
