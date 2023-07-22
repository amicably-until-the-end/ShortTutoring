package org.softwaremaestro.data.question_upload.model

import com.google.gson.annotations.SerializedName

data class QuestionUploadRequestDto(
    @SerializedName("problemDescription") var problemDescription: String,
    @SerializedName("problemBase64Image") var problemBase64Image: String,
    @SerializedName("problemSchoolLevel") var problemSchoolLevel: String,
    @SerializedName("problemSchoolSubject") var problemSchoolSubject: String,
    @SerializedName("problemSchoolChapter") var problemSchoolChapter: String,
    @SerializedName("problemDifficulty") var problemDifficulty: String
)
