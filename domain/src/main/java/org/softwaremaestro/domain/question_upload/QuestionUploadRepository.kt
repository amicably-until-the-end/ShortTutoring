package org.softwaremaestro.domain.question_upload

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_upload.entity.QuestionUploadResultVO
import org.softwaremaestro.domain.question_upload.entity.QuestionUploadVO
import org.softwaremaestro.domain.question_upload.entity.TeacherPickReqVO
import org.softwaremaestro.domain.question_upload.entity.TeacherPickResVO
import org.softwaremaestro.domain.question_upload.entity.TeacherVO


interface QuestionUploadRepository {
    suspend fun uploadQuestion(questionUploadVO: QuestionUploadVO): Flow<BaseResult<QuestionUploadResultVO, String>>
    suspend fun getTeacherList(questionId: String): Flow<BaseResult<List<TeacherVO>, String>>

    suspend fun pickTeacher(
        chattingId: String, questionId: String
    ): Flow<BaseResult<String, String>>
}