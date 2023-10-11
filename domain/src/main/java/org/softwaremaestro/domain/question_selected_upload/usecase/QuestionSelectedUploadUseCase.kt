package org.softwaremaestro.domain.question_selected_upload.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_selected_upload.QuestionSelectedUploadRepository
import org.softwaremaestro.domain.question_selected_upload.entity.QuestionSelectedUploadVO
import javax.inject.Inject

class QuestionSelectedUploadUseCase @Inject constructor(private val repository: QuestionSelectedUploadRepository) {
    suspend fun execute(questionSelectedUploadVO: QuestionSelectedUploadVO): Flow<BaseResult<String, String>> =
        repository.uploadQuestionSelected(questionSelectedUploadVO)
}