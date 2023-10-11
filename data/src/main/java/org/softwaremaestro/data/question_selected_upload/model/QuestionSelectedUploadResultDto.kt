package org.softwaremaestro.data.question_selected_upload.model

import com.google.gson.annotations.SerializedName
import org.softwaremaestro.domain.question_selected_upload.entity.ProblemVO
import org.softwaremaestro.domain.question_selected_upload.entity.StudentVO

data class QuestionSelectedUploadResultDto(
    @SerializedName("id") val id: String?,
    @SerializedName("student") val student: StudentVO?,
    @SerializedName("teacherIds") val teacherIds: List<String>?,
    @SerializedName("problem") val problem: ProblemVO?,
    @SerializedName("hopeTutorialTime") val hopeTutorialTime: List<String>?,
    @SerializedName("hopeImmediately") val hopeImmediately: Boolean?,
    @SerializedName("chattingId") val chattingId: String?,
)
