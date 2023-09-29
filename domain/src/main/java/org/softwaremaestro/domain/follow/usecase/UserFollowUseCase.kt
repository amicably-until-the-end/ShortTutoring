package org.softwaremaestro.domain.follow.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.follow.FollowRepository
import javax.inject.Inject

class UserFollowUseCase @Inject constructor(private val followRepository: FollowRepository) {
    suspend fun followUser(userId: String): Flow<BaseResult<String, String>> {
        return followRepository.followUser(userId)
    }
}