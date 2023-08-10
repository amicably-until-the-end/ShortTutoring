package org.softwaremaestro.domain.user_follow

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult

interface UserFollowRepository {

    suspend fun followUser(userId: String): Flow<BaseResult<String, String>>
    suspend fun unfollowUser(userId: String): Flow<BaseResult<String, String>>
}