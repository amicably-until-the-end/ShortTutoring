package org.softwaremaestro.domain.question_upload.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_upload.QuestionUploadRepository
import org.softwaremaestro.domain.question_upload.entity.TeacherVO
import javax.inject.Inject

class TeacherListGetUseCase @Inject constructor(private val repository: QuestionUploadRepository) {
    suspend fun execute(questionId: String): Flow<BaseResult<List<TeacherVO>, String>> =
        repository.getTeacherList(questionId)
}