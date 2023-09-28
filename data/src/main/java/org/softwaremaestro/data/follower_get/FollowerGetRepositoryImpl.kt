package org.softwaremaestro.data.follower_get

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.follower_get.model.asDomain
import org.softwaremaestro.data.follower_get.remote.FollowerGetApi
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.follower_get.FollowerGetRepository
import org.softwaremaestro.domain.follower_get.entity.FollowerGetResponseVO
import javax.inject.Inject

class FollowerGetRepositoryImpl @Inject constructor(private val followerGetApi: FollowerGetApi) :
    FollowerGetRepository {

    override suspend fun getFollower(userId: String): Flow<BaseResult<List<FollowerGetResponseVO>, String>> {
        return flow {
            val response = followerGetApi.getFollowers(userId)
            val body = response.body()!!
            if (body.success == true) {
                body.data
                    ?.map { it.asDomain() }
                    ?.let {
                        emit(BaseResult.Success(it))
                    }
            } else {
                val errorString =
                    "error in ${this@FollowerGetRepositoryImpl::class.java.name}\n" +
                            "message: ${response.body()!!.message}"
                Log.d(this@FollowerGetRepositoryImpl::class.java.name, errorString)
            }
        }
    }
}