package org.softwaremaestro.domain.question_upload.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_upload.QuestionUploadRepository
import org.softwaremaestro.domain.question_upload.entity.QuestionUploadResultVO
import org.softwaremaestro.domain.question_upload.entity.QuestionUploadVO
import javax.inject.Inject


class QuestionUploadUseCase @Inject constructor(private val repository: QuestionUploadRepository) {

    suspend fun execute(questionUploadVO: QuestionUploadVO): Flow<BaseResult<QuestionUploadResultVO, String>> =
        repository.uploadQuestion(questionUploadVO)
}