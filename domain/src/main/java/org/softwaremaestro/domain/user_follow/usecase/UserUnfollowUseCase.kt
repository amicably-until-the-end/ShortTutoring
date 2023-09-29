package org.softwaremaestro.domain.user_follow.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.user_follow.UserFollowRepository
import javax.inject.Inject

class UserUnfollowUseCase @Inject constructor(private val userFollowRepository: UserFollowRepository) {
    suspend fun unfollowUser(userId: String): Flow<BaseResult<String, String>> {
        return userFollowRepository.unfollowUser(userId)
    }
}