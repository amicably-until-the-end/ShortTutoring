package org.softwaremaestro.data.question_get.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class QuestionGetResultDto(
    @SerializedName("problem_base64_image") val problemBase64Image: String,
    @SerializedName("problem_school_level") val problemSchoolLevel: String,
    @SerializedName("problem_school_chapter") val problemSchoolChapter: String,
    @SerializedName("problem_school_subject") val problemSchoolSubject: String,
    @SerializedName("problem_difficulty") val problemDifficulty: String,
    @SerializedName("reviews") val reviews: List<String>,
    @SerializedName("created_at") val createdAt: LocalDateTime,
    @SerializedName("problem_description") val problemDescription: String,
    @SerializedName("teacher_ids") val teacherIds: List<String>,
    @SerializedName("student_id") val studentId: String
)
