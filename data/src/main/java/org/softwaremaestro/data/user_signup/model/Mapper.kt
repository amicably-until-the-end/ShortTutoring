package org.softwaremaestro.data.user_signup.model

import org.softwaremaestro.domain.user_signup.entity.StudentSignupRequestVO
import org.softwaremaestro.domain.user_signup.entity.TeacherSignupRequestVO

object Mapper {
    fun toDto(studentSignupRequestVO: StudentSignupRequestVO): StudentSignupRequestDto {
        return StudentSignupRequestDto(
            vendor = studentSignupRequestVO.vendor,
            accessToken = studentSignupRequestVO.accessToken,
            name = studentSignupRequestVO.name,
            bio = studentSignupRequestVO.bio,
            schoolLevel = studentSignupRequestVO.schoolLevel,
            schoolGrade = studentSignupRequestVO.schoolGrade
        )
    }

    fun toDto(teacherSignupRequestVO: TeacherSignupRequestVO): TeacherSignupRequestDto {
        return TeacherSignupRequestDto(
            vendor = teacherSignupRequestVO.vendor,
            accessToken = teacherSignupRequestVO.accessToken,
            name = teacherSignupRequestVO.name,
            bio = teacherSignupRequestVO.bio,
            schoolName = teacherSignupRequestVO.schoolName,
            schoolDivision = teacherSignupRequestVO.schoolDivision,
            schoolDepartment = teacherSignupRequestVO.schoolDepartment,
            schoolGrade = teacherSignupRequestVO.schoolGrade
        )
    }
}

fun StudentSignupRequestVO.asDto(): StudentSignupRequestDto {
    return Mapper.toDto(this)
}

fun TeacherSignupRequestVO.asDto(): TeacherSignupRequestDto {
    return Mapper.toDto(this)
}
