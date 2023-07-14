package org.softwaremaestro.domain.question_upload.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_upload.QuestionUploadRepository
import org.softwaremaestro.domain.question_upload.entity.TeacherPickReqVO
import javax.inject.Inject

class TeacherPickUseCase @Inject constructor(private val questionUploadRepository: QuestionUploadRepository) {
    suspend fun execute(teacherPickReqVO: TeacherPickReqVO): Flow<BaseResult<String, String>> {
        return questionUploadRepository.pickTeacher(teacherPickReqVO)
    }
}