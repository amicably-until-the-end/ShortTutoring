package org.softwaremaestro.data.question_upload.model

import com.google.gson.annotations.SerializedName

data class QuestionUploadRequestDto(
    @SerializedName("student_id") var studentId: String,
    @SerializedName("problem_description") var description: String,
    @SerializedName("problem_base64_image") var imageEncoding: String,
    @SerializedName("problem_school_level") var schoolLevel: String,
    @SerializedName("problem_school_subject") var schoolSubject: String,
    @SerializedName("problem_school_chapter") var schoolChapter: String,
    @SerializedName("problem_difficulty") var problemDifficulty: String,
)
