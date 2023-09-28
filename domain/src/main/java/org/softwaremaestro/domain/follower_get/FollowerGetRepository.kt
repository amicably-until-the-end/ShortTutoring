package org.softwaremaestro.domain.follower_get

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.follower_get.entity.FollowerGetResponseVO

interface FollowerGetRepository {
    suspend fun getFollower(
        userId: String
    ): Flow<BaseResult<List<FollowerGetResponseVO>, String>>
}