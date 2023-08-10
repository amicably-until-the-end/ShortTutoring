package org.softwaremaestro.domain.following_get.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.following_get.FollowingGetRepository
import org.softwaremaestro.domain.following_get.entity.FollowingGetResponseVO
import javax.inject.Inject

class FollowingGetUseCase @Inject constructor(private val followingGetRepository: FollowingGetRepository) {

    suspend fun execute(
        userId: String
    ): Flow<BaseResult<List<FollowingGetResponseVO>, String>> {
        return followingGetRepository.getFollowing(userId)
    }
}