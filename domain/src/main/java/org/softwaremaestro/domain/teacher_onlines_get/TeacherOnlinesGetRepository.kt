package org.softwaremaestro.domain.teacher_onlines_get

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.teacher_onlines_get.entity.TeacherOnlineVO


interface TeacherOnlinesGetRepository {
    suspend fun getTeacherOnlines(): Flow<BaseResult<List<TeacherOnlineVO>, String>>
}