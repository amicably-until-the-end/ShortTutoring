package org.softwaremaestro.data.question_selected_upload.remote

import org.softwaremaestro.data.common.utils.WrappedResponse
import org.softwaremaestro.data.question_selected_upload.model.QuestionSelectedUploadReqDto
import org.softwaremaestro.data.question_selected_upload.model.QuestionSelectedUploadResultDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface QuestionSelectedUploadApi {
    @POST("/student/question/create/selected")
    suspend fun uploadQuestionSelected(
        @Body questionSelectedUploadReqDto: QuestionSelectedUploadReqDto
    ): Response<WrappedResponse<QuestionSelectedUploadResultDto>>
}