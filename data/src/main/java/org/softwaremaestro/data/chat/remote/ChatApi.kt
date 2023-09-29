package org.softwaremaestro.data.chat.remote

import org.softwaremaestro.data.chat.model.ChatRoomDto
import org.softwaremaestro.data.chat.model.ChatRoomListDto
import org.softwaremaestro.data.classroom.model.TutoringInfoDto
import org.softwaremaestro.data.common.utils.WrappedListResponse
import org.softwaremaestro.data.common.utils.WrappedResponse
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.QuestionState
import org.softwaremaestro.domain.chat.entity.QuestionType
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApi {

    @GET("/chatting/list")
    suspend fun getRoomList(): Response<WrappedListResponse<ChatRoomDto>>

    @GET("/chatting/{roomId}")
    suspend fun getRoom(@Path("roomId") roomId: String): Response<WrappedResponse<ChatRoomDto>>

}