package org.softwaremaestro.domain.follow

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.follow.entity.FollowerGetResponseVO
import org.softwaremaestro.domain.best_teacher_get.entity.TeacherVO

interface FollowRepository {

    suspend fun followUser(userId: String): Flow<BaseResult<String, String>>
    suspend fun unfollowUser(userId: String): Flow<BaseResult<String, String>>
    suspend fun getFollower(userId: String): Flow<BaseResult<List<FollowerGetResponseVO>, String>>
    suspend fun getFollowing(userId: String): Flow<BaseResult<List<TeacherVO>, String>>
}