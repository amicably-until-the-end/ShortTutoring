package org.softwaremaestro.data.question_upload

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.common.utils.toStringWithTimeZone
import org.softwaremaestro.data.question_upload.model.PickTeacherReqDto
import org.softwaremaestro.data.question_upload.model.asDto
import org.softwaremaestro.data.question_upload.remote.QuestionUploadApi
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_upload.QuestionUploadRepository
import org.softwaremaestro.domain.question_upload.entity.QuestionUploadResultVO
import org.softwaremaestro.domain.question_upload.entity.QuestionUploadVO
import org.softwaremaestro.domain.question_upload.entity.TeacherVO
import java.time.LocalDateTime
import javax.inject.Inject

private const val EMPTY_STRING = "undefined"

class QuestionUploadRepositoryImpl @Inject constructor(private val questionUploadApi: QuestionUploadApi) :
    QuestionUploadRepository {

    override suspend fun uploadQuestion(questionUploadVO: QuestionUploadVO): Flow<BaseResult<QuestionUploadResultVO, String>> {
        return flow {
            val dto = questionUploadVO.asDto()
            val response = questionUploadApi.uploadQuestion(dto)
            if (response.isSuccessful) {
                val body = response.body()
                val resultVO = QuestionUploadResultVO(body?.data?.questionId ?: "")
                emit(BaseResult.Success(resultVO))
            } else {
                val errorString = "error"
                emit(BaseResult.Error(errorString))
            }
        }
    }

    override suspend fun getTeacherList(questionId: String): Flow<BaseResult<List<TeacherVO>, String>> {
        return flow {
            val response = questionUploadApi.getTeacherList(questionId)
            val body = response.body()
            if (response.isSuccessful && body?.success == true) {
                val data = body.data
                val teacherDtoList = data?.teacherList!!
                val teacherList = teacherDtoList.map { teacher ->
                    TeacherVO(
                        name = teacher.name,
                        school = "서울대학교", // Todo
                        likes = 23, // Todo
                        imageUrl = EMPTY_STRING,
                        teacherId = teacher.id
                    )
                }
                emit(BaseResult.Success(teacherList))
            } else {
                emit(BaseResult.Error("error"))
            }
        }
    }

    override suspend fun pickTeacher(
        startTime: LocalDateTime, endTime: LocalDateTime, chattingId: String, questionId: String
    ): Flow<BaseResult<String, String>> {
        return flow {
            val response =
                questionUploadApi.pickTeacher(
                    questionId = questionId,
                    PickTeacherReqDto(
                        startTime = startTime.toStringWithTimeZone(),
                        endTime = endTime.toStringWithTimeZone(),
                        chattingId = chattingId
                    )
                )

            val body = response.body()
            if (response.isSuccessful && body?.success == true) {
                val tutoringInfo = body.data

                //val teacherPickResVO = tutoringInfo?.asDomain()!!

                emit(BaseResult.Success("Success"))
            } else {
                emit(BaseResult.Error("error"))
            }
        }
    }
}
