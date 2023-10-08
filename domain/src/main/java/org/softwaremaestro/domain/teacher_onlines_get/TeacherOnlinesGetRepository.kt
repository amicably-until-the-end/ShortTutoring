package org.softwaremaestro.domain.teacher_onlines_get

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.teacher_get.entity.TeacherVO


interface TeacherOnlinesGetRepository {
    suspend fun getTeacherOnlines(): Flow<BaseResult<List<TeacherVO>, String>>
}