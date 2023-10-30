package org.softwaremaestro.domain.login

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.login.entity.StudentRegisterVO
import org.softwaremaestro.domain.login.entity.TeacherRegisterVO

interface RegisterRepository {
    suspend fun registerStudent(studentRegisterVO: StudentRegisterVO): Flow<BaseResult<String, String>>

    suspend fun registerTeacher(teacherRegisterVO: TeacherRegisterVO): Flow<BaseResult<String, String>>

    suspend fun withdraw(): Flow<BaseResult<Boolean, String>>
}