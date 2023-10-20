package org.softwaremaestro.domain.best_teacher_get.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.best_teacher_get.BestTeacherRepository
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.best_teacher_get.entity.TeacherVO
import javax.inject.Inject

class BestTeacherGetUseCase @Inject constructor(private val bestTeacherRepository: BestTeacherRepository) {

    suspend fun execute(): Flow<BaseResult<List<TeacherVO>, String>> {
        return bestTeacherRepository.getBestTeacher()
    }
}