package org.softwaremaestro.data.question_upload.model

import com.google.gson.annotations.SerializedName

data class TeacherListDto(
    @SerializedName("teacher_ids") val teacherList: List<String>
)
