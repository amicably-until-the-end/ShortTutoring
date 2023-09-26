package org.softwaremaestro.data.login

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.common.utils.SavedToken
import org.softwaremaestro.data.login.model.StudentRegisterReqDto
import org.softwaremaestro.data.login.model.TeacherRegisterReqDto
import org.softwaremaestro.data.login.remote.RegisterApi
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.login.RegisterRepository
import org.softwaremaestro.domain.login.entity.StudentRegisterVO
import org.softwaremaestro.domain.login.entity.TeacherRegisterVO
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val registerApi: RegisterApi,
    private val savedToken: SavedToken
) :
    RegisterRepository {
    override suspend fun registerStudent(studentRegisterVO: StudentRegisterVO): Flow<BaseResult<String, String>> {

        return flow {
            val token = savedToken.getTokenInfo()
            val response = registerApi.registerStudent(
                StudentRegisterReqDto(
                    bio = studentRegisterVO.bio,
                    name = studentRegisterVO.name,
                    accessToken = token.token!!,
                    vendor = token.vendor!!,
                    schoolLevel = studentRegisterVO.schoolLevel,
                    schoolGrade = studentRegisterVO.schoolGrade,
                )
            )
            if (response.isSuccessful && response.body()?.success == true) {
                emit(
                    BaseResult.Success(
                        "success"
                    )
                )
            } else {
                emit(BaseResult.Error("error"))
            }
        }
    }

    override suspend fun registerTeacher(teacherRegisterVO: TeacherRegisterVO): Flow<BaseResult<String, String>> {
        return flow {
            val token = savedToken.getTokenInfo()
            val response = registerApi.registerTeacher(
                TeacherRegisterReqDto(
                    teacherRegisterVO.bio,
                    teacherRegisterVO.name,
                    token.token!!,
                    token.vendor!!,
                    teacherRegisterVO.schoolName,
                    teacherRegisterVO.schoolDivision,
                    teacherRegisterVO.schoolDepartment,
                    teacherRegisterVO.schoolGrade
                )
            )
            val body = response.body()!!
            if (body.success == true) {
                emit(BaseResult.Success("success"))
            } else {
                emit(BaseResult.Error("error"))

            }

        }
    }

}