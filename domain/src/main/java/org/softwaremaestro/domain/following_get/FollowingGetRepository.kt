package org.softwaremaestro.domain.following_get

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.following_get.entity.FollowingGetResponseVO

interface FollowingGetRepository {
    suspend fun getFollowing(
        userId: String
    ): Flow<BaseResult<List<FollowingGetResponseVO>, String>>
}