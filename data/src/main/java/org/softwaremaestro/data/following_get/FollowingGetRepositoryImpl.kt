package org.softwaremaestro.data.following_get

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.following_get.model.asDomain
import org.softwaremaestro.data.following_get.remote.FollowingGetApi
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.following_get.FollowingGetRepository
import org.softwaremaestro.domain.following_get.entity.FollowingGetResponseVO
import javax.inject.Inject

class FollowingGetRepositoryImpl @Inject constructor(private val followingGetApi: FollowingGetApi) :
    FollowingGetRepository {

    override suspend fun getFollowing(userId: String): Flow<BaseResult<List<FollowingGetResponseVO>, String>> {
        return flow {
            val response = followingGetApi.getFollowing(userId)
            val body = response.body()!!
            if (body.success == true) {
                body.data
                    ?.map { it.asDomain() }
                    ?.let {
                        emit(BaseResult.Success(it))
                    }
            } else {
                val errorString =
                    "error in ${this@FollowingGetRepositoryImpl::class.java.name}\n" +
                            "message: ${response.body()!!.message}"
                Log.d("Error", errorString)
            }
        }
    }
}