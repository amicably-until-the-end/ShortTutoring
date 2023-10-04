package org.softwaremaestro.domain.teacher_onlines_get.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.teacher_onlines_get.TeacherOnlinesGetRepository
import org.softwaremaestro.domain.teacher_onlines_get.entity.TeacherOnlineVO
import javax.inject.Inject

class TeacherOnlinesGetUseCase @Inject constructor(private val repository: TeacherOnlinesGetRepository) {

    suspend fun execute(): Flow<BaseResult<List<TeacherOnlineVO>, String>> {
        return repository.getTeacherOnlines()
    }
}