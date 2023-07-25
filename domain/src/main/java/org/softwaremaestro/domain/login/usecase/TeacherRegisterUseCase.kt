package org.softwaremaestro.domain.login.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.login.LoginRepository
import org.softwaremaestro.domain.login.RegisterRepository
import org.softwaremaestro.domain.login.entity.TeacherRegisterVO
import javax.inject.Inject


class TeacherRegisterUseCase @Inject constructor(private val repository: RegisterRepository) {
    suspend fun execute(teacherRegisterVO: TeacherRegisterVO): Flow<BaseResult<String, String>> =
        repository.registerTeacher(teacherRegisterVO)
}