package org.softwaremaestro.data.chat.remote

import org.softwaremaestro.data.chat.model.ChatRoomDto
import org.softwaremaestro.data.classroom.model.TutoringInfoDto
import org.softwaremaestro.data.common.utils.WrappedResponse
import org.softwaremaestro.domain.chat.entity.QuestionState
import org.softwaremaestro.domain.chat.entity.QuestionType
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApi {

    @GET("/chat/roomlist")
    suspend fun getRoomList(
        @Query("type") questionType: QuestionType,
        @Query("state") questionState: QuestionState
    ): Response<WrappedResponse<List<ChatRoomDto>>>

}