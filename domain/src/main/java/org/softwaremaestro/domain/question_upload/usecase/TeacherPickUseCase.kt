package org.softwaremaestro.domain.question_upload.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_upload.QuestionUploadRepository
import javax.inject.Inject

class TeacherPickUseCase @Inject constructor(private val questionUploadRepository: QuestionUploadRepository) {
    suspend fun execute(teacherId: String): Flow<BaseResult<String, String>> {
        return questionUploadRepository.pickTeacher(teacherId)
    }
}