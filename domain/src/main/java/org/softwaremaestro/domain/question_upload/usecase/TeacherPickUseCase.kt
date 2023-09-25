package org.softwaremaestro.domain.question_upload.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_upload.QuestionUploadRepository
import org.softwaremaestro.domain.question_upload.entity.TeacherPickReqVO
import org.softwaremaestro.domain.question_upload.entity.TeacherPickResVO
import javax.inject.Inject

class TeacherPickUseCase @Inject constructor(private val questionUploadRepository: QuestionUploadRepository) {
    suspend fun execute(
        chattingId: String,
        questionId: String
    ): Flow<BaseResult<String, String>> {
        return questionUploadRepository.pickTeacher(chattingId, questionId)
    }
}