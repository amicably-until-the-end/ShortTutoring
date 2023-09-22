package org.softwaremaestro.domain.question_selected_upload

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_selected_upload.entity.QuestionSelectedUploadResultVO
import org.softwaremaestro.domain.question_selected_upload.entity.QuestionSelectedUploadVO


interface QuestionSelectedUploadRepository {
    suspend fun uploadQuestionSelected(questionSelectedUploadVO: QuestionSelectedUploadVO): Flow<BaseResult<QuestionSelectedUploadResultVO, String>>
}