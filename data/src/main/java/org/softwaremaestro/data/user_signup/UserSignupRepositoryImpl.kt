package org.softwaremaestro.data.user_signup

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.user_signup.model.asDto
import org.softwaremaestro.data.user_signup.remote.UserSignupApi
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.user_signup.UserSignupRepository
import org.softwaremaestro.domain.user_signup.entity.StudentSignupRequestVO
import org.softwaremaestro.domain.user_signup.entity.StudentSignupResultVO
import org.softwaremaestro.domain.user_signup.entity.TeacherSignupRequestVO
import org.softwaremaestro.domain.user_signup.entity.TeacherSignupResultVO
import javax.inject.Inject

class UserSignupRepositoryImpl @Inject constructor(private val userSignupApi: UserSignupApi) :
    UserSignupRepository {

    override suspend fun signupStudent(studentSignupRequestVO: StudentSignupRequestVO): Flow<BaseResult<StudentSignupResultVO, String>> {
        return flow {
            val dto = studentSignupRequestVO.asDto()
            val response = userSignupApi.signupStudent(dto)
            if (response.isSuccessful) {
                val body = response.body()
                val resultVO = StudentSignupResultVO(body?.data?.token)
                emit(BaseResult.Success(resultVO))
            } else {
                emit(BaseResult.Error(response.message()))
            }
        }
    }

    override suspend fun signupTeacher(teacherSignupRequestVO: TeacherSignupRequestVO): Flow<BaseResult<TeacherSignupResultVO, String>> {
        return flow {
            val dto = teacherSignupRequestVO.asDto()
            val response = userSignupApi.signupTeacher(dto)
            if (response.isSuccessful) {
                val body = response.body()
                val resultVO = TeacherSignupResultVO(body?.data?.token)
                emit(BaseResult.Success(resultVO))
            } else {
                emit(BaseResult.Error(response.message()))
            }
        }
    }
}