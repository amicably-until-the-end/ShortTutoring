package org.softwaremaestro.domain.user_signup

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.user_signup.entity.StudentSignupRequestVO
import org.softwaremaestro.domain.user_signup.entity.StudentSignupResultVO
import org.softwaremaestro.domain.user_signup.entity.TeacherSignupRequestVO
import org.softwaremaestro.domain.user_signup.entity.TeacherSignupResultVO

interface UserSignupRepository {
    suspend fun signupStudent(studentSignupRequestVO: StudentSignupRequestVO): Flow<BaseResult<StudentSignupResultVO, String>>
    suspend fun signupTeacher(teacherSignupRequestVO: TeacherSignupRequestVO): Flow<BaseResult<TeacherSignupResultVO, String>>
}