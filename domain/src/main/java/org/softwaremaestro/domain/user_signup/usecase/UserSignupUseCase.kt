package org.softwaremaestro.domain.user_signup.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.user_signup.UserSignupRepository
import org.softwaremaestro.domain.user_signup.entity.StudentSignupRequestVO
import org.softwaremaestro.domain.user_signup.entity.StudentSignupResultVO
import org.softwaremaestro.domain.user_signup.entity.TeacherSignupRequestVO
import org.softwaremaestro.domain.user_signup.entity.TeacherSignupResultVO
import javax.inject.Inject


class UserSignupUseCase @Inject constructor(private val repository: UserSignupRepository) {

    suspend fun execute(studentSignupRequestVO: StudentSignupRequestVO): Flow<BaseResult<StudentSignupResultVO, String>> =
        repository.signupStudent(studentSignupRequestVO)

    suspend fun execute(teacherSignupRequestVO: TeacherSignupRequestVO): Flow<BaseResult<TeacherSignupResultVO, String>> =
        repository.signupTeacher(teacherSignupRequestVO)
}