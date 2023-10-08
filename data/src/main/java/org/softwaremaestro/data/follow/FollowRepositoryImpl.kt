package org.softwaremaestro.data.follow

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.follow.model.asDomain
import org.softwaremaestro.data.follow.remote.FollowApi
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.follow.FollowRepository
import org.softwaremaestro.domain.follow.entity.FollowerGetResponseVO
import org.softwaremaestro.domain.teacher_get.entity.TeacherVO
import javax.inject.Inject

class FollowRepositoryImpl @Inject constructor(private val followApi: FollowApi) :
    FollowRepository {

    override suspend fun followUser(userId: String): Flow<BaseResult<String, String>> {
        return flow {
            val response = followApi.followUser(userId)
            val body = response.body()
            if (body?.success == true) {
                emit(BaseResult.Success("follow user succeed"))
            } else {
                val errorString = "error in ${this@FollowRepositoryImpl::class.java.name}\n" +
                        "message : ${body?.message}"
                emit(BaseResult.Error(errorString))
            }
        }
    }

    override suspend fun unfollowUser(userId: String): Flow<BaseResult<String, String>> {
        return flow {
            val response = followApi.unfollowUser(userId)
            val body = response.body()
            if (body?.success == true) {
                emit(BaseResult.Success("unfollow user succeed"))
            } else {
                val errorString = "error in ${this@FollowRepositoryImpl::class.java.name}\n" +
                        "message : ${body?.message}"
                emit(BaseResult.Error(errorString))
            }
        }
    }

    override suspend fun getFollower(userId: String): Flow<BaseResult<List<FollowerGetResponseVO>, String>> {
        return flow {
            val response = followApi.getFollowers(userId)
            val body = response.body()!!
            if (body.success == true) {
                body.data
                    ?.map { it.asDomain() }
                    ?.let {
                        emit(BaseResult.Success(it))
                    }
            } else {
                val errorString =
                    "error in ${this@FollowRepositoryImpl::class.java.name}\n" +
                            "message: ${response.body()!!.message}"
                Log.d(this@FollowRepositoryImpl::class.java.name, errorString)
            }
        }
    }

    override suspend fun getFollowing(userId: String): Flow<BaseResult<List<TeacherVO>, String>> {
        return flow {
            val response = followApi.getFollowing(userId)
            val body = response.body()!!
            if (body.success == true) {
                body.data
                    ?.map { it.asDomain() }
                    ?.let {
                        emit(BaseResult.Success(it))
                    }
            } else {
                val errorString =
                    "error in ${this@FollowRepositoryImpl::class.java.name}\n" +
                            "message: ${response.body()!!.message}"
                Log.d(this@FollowRepositoryImpl::class.java.name, errorString)
            }
        }
    }
}