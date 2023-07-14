package org.softwaremaestro.data.question_upload

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.question_upload.model.PickTeacherReqDto
import org.softwaremaestro.data.question_upload.model.QuestionUploadRequestDto
import org.softwaremaestro.data.question_upload.remote.QuestionUploadApi
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_upload.QuestionUploadRepository
import org.softwaremaestro.domain.question_upload.entity.QuestionUploadResultVO
import org.softwaremaestro.domain.question_upload.entity.QuestionUploadVO
import org.softwaremaestro.domain.question_upload.entity.TeacherPickReqVO
import org.softwaremaestro.domain.question_upload.entity.TeacherVO
import javax.inject.Inject

class QuestionUploadRepositoryImpl @Inject constructor(private val questionUploadApi: QuestionUploadApi) :
    QuestionUploadRepository {


    override suspend fun uploadQuestion(questionUploadVO: QuestionUploadVO): Flow<BaseResult<QuestionUploadResultVO, String>> {
        return flow {
            val dto = QuestionUploadRequestDto(
                questionUploadVO.studentId,
                questionUploadVO.description,
                questionUploadVO.imageEncoding,
                questionUploadVO.schoolLevel,
                questionUploadVO.schoolSubject,
                questionUploadVO.schoolChapter,
                questionUploadVO.problemDifficulty,
            )
            val response = questionUploadApi.uploadQuestion(questionUploadVO.studentId, dto)
            Log.d("mymymy", "${response.toString()} is res in imple")
            if (response.isSuccessful) {
                val body = response.body()!!
                val resultVO = QuestionUploadResultVO(body?.data?.questionId!!)
                emit(BaseResult.Success(resultVO))
            } else {
                val errorString = "error"
                emit(BaseResult.Error(errorString))
            }
        }
    }

    override suspend fun getTeacherList(questionId: String): Flow<BaseResult<List<TeacherVO>, String>> {
        return flow {
            Log.d("mymymy", "${questionId} is question id in before api call")
            val response = questionUploadApi.getTeacherList(questionId)
            if (response.isSuccessful) {
                val data = response.body()?.data
                val teacherDtoList = data ?: emptyList()
                val teacherList = teacherDtoList.map { teacherDto ->
                    TeacherVO(
                        name = "undefined", // Set the appropriate values for name, school, bio, imageUrl, and teacherId
                        school = "undefined",
                        bio = "undefined",
                        imageUrl = "undefined",
                        teacherId = teacherDto.id
                    )
                }
                emit(BaseResult.Success(teacherList))
            } else {
                emit(BaseResult.Error("error"))
            }
        }
    }

    override suspend fun pickTeacher(
        VO: TeacherPickReqVO
    ): Flow<BaseResult<String, String>> {
        return flow {
            val response =
                questionUploadApi.pickTeacher(
                    PickTeacherReqDto(
                        VO.studentId,
                        VO.teacherId,
                        VO.questionId
                    )
                )
            Log.d("mymymy", "pickTeacher ${response.body()} is res in imple")

            if (response.isSuccessful) {
                val tutoringId = response.body()?.data?.tutoringId ?: ""
                Log.d("mymymy", "tutoring Id in imple ${tutoringId}")
                emit(BaseResult.Success(tutoringId))
            } else {
                emit(BaseResult.Error("error"))
            }
        }
    }
}
