package org.softwaremaestro.data.question_upload

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.question_upload.model.QuestionUploadRequestDto
import org.softwaremaestro.data.question_upload.remote.QuestionUploadApi
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_upload.QuestionUploadRepository
import org.softwaremaestro.domain.question_upload.entity.QuestionUploadResultVO
import org.softwaremaestro.domain.question_upload.entity.QuestionUploadVO
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
            if (response.isSuccessful) {
                val body = response.body()!!
                val resultVO = QuestionUploadResultVO(body?.questionId!!)
                emit(BaseResult.Success(resultVO))
            } else {
                Log.d("mymymy", response.toString()!!)
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
                val body = response.body()
                val teacherDtoList = body ?: emptyList()
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

    override suspend fun pickTeacher(teacherId: String): Flow<BaseResult<String, String>> {
        TODO("Not yet implemented")
    }
}