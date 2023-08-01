package org.softwaremaestro.data.login

import android.util.Log
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
            val response = registerApi.registerStudent(
                StudentRegisterReqDto(
                    "testBio",
                    "testName",
                    "student",
                    savedToken.getTokenInfo().token!!,
                )
            )
            if (response.isSuccessful && !response.body()!!.error) {
                emit(BaseResult.Success(response.body()!!.message))
            } else {
                val errorString =
                    "error in ${this@RegisterRepositoryImpl::class.java.name}\n" +
                            "message: ${response.body()!!.message}"
                Log.d("Error", errorString)
            }
        }
    }

    override suspend fun registerTeacher(teacherRegisterVO: TeacherRegisterVO): Flow<BaseResult<String, String>> {
        return flow {
            val response = registerApi.registerTeacher(
                TeacherRegisterReqDto(
                    teacherRegisterVO.univ,
                    teacherRegisterVO.colledge,
                    teacherRegisterVO.major,
                    teacherRegisterVO.admissonYear
                )
            )
            if (!response.isSuccessful || response.body()?.error == true) {
                emit(BaseResult.Error("error"))
            } else {
                emit(BaseResult.Success(response.body()!!.message))
            }

        }
    }

}