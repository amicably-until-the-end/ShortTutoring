package org.softwaremaestro.domain.follower_get.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.follower_get.FollowerGetRepository
import org.softwaremaestro.domain.follower_get.entity.FollowerGetResponseVO
import javax.inject.Inject

class FollowerGetUseCase @Inject constructor(private val followerGetRepository: FollowerGetRepository) {

    suspend fun execute(
        userId: String
    ): Flow<BaseResult<List<FollowerGetResponseVO>, String>> {
        return followerGetRepository.getFollower(userId)
    }
}