package org.softwaremaestro.domain.follow.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.follow.FollowRepository
import org.softwaremaestro.domain.follow.entity.FollowingGetResponseVO
import javax.inject.Inject

class FollowingGetUseCase @Inject constructor(private val followRepository: FollowRepository) {

    suspend fun execute(
        userId: String
    ): Flow<BaseResult<List<FollowingGetResponseVO>, String>> {
        return followRepository.getFollowing(userId)
    }
}