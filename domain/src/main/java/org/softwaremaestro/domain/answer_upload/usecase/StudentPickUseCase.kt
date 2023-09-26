package org.softwaremaestro.domain.answer_upload.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.answer_upload.AnswerUploadRepository
import org.softwaremaestro.domain.answer_upload.entity.StudentPickResultVO
import org.softwaremaestro.domain.common.BaseResult
import javax.inject.Inject

class StudentPickUseCase @Inject constructor(private val repository: AnswerUploadRepository) {
    suspend fun execute(questionId: String): Flow<BaseResult<StudentPickResultVO, String>> {
        return repository.pickStudent(questionId)
    }
}