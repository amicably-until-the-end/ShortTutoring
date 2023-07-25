package org.softwaremaestro.domain.login.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.login.LoginRepository
import org.softwaremaestro.domain.login.RegisterRepository
import org.softwaremaestro.domain.login.entity.StudentRegisterVO
import javax.inject.Inject


class StudentRegisterUseCase @Inject constructor(private val repository: RegisterRepository) {
    suspend fun execute(studentRegisterVO: StudentRegisterVO): Flow<BaseResult<String, String>> =
        repository.registerStudent(studentRegisterVO)
}