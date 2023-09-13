package org.softwaremaestro.data.user_follow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.user_follow.remote.UserFollowApi
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.user_follow.SUCCESS_USER_FOLLOW
import org.softwaremaestro.domain.user_follow.SUCCESS_USER_UNFOLLOW
import org.softwaremaestro.domain.user_follow.UserFollowRepository
import javax.inject.Inject

class UserFollowRepositoryImpl @Inject constructor(private val userFollowApi: UserFollowApi) :
    UserFollowRepository {

    override suspend fun followUser(userId: String): Flow<BaseResult<String, String>> {
        return flow {
            val response = userFollowApi.followUser(userId)
            val body = response.body()
            if (body?.success == true) {
                emit(BaseResult.Success(SUCCESS_USER_FOLLOW))
            } else {
                val errorString = "error"
                emit(BaseResult.Error(errorString))
            }
        }
    }

    override suspend fun unfollowUser(userId: String): Flow<BaseResult<String, String>> {
        return flow {
            val response = userFollowApi.unfollowUser(userId)
            val body = response.body()
            if (body?.success == true) {
                emit(BaseResult.Success(SUCCESS_USER_UNFOLLOW))
            } else {
                val errorString = "error"
                emit(BaseResult.Error(errorString))
            }
        }
    }
}