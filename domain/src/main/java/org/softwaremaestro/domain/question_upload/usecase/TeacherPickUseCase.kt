package org.softwaremaestro.domain.question_upload.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_upload.QuestionUploadRepository
import org.softwaremaestro.domain.question_upload.entity.TeacherPickResVO
import java.time.LocalDateTime
import javax.inject.Inject

class TeacherPickUseCase @Inject constructor(private val questionUploadRepository: QuestionUploadRepository) {
    suspend fun execute(
        time: LocalDateTime,
        chattingId: String,
        questionId: String
    ): Flow<BaseResult<TeacherPickResVO, String>> {
        return questionUploadRepository.pickTeacher(time, chattingId, questionId)
    }
}