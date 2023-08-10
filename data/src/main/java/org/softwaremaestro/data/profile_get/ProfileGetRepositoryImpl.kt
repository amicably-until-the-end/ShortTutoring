package org.softwaremaestro.data.profile_get

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.profile_get.model.asDomain
import org.softwaremaestro.data.profile_get.remote.ProfileGetApi
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.profile_get.ProfileGetRepository
import org.softwaremaestro.domain.profile_get.entity.ProfileGetResponseVO
import javax.inject.Inject

class ProfileGetRepositoryImpl @Inject constructor(private val profileGetApi: ProfileGetApi) :
    ProfileGetRepository {

    override suspend fun getProfile(): Flow<BaseResult<ProfileGetResponseVO, String>> {
        return flow {
            val response = profileGetApi.getProfile()
            val body = response.body()!!
            if (body.success == true) {
                body.data?.asDomain()?.let {
                    emit(BaseResult.Success(it))
                }
            } else {
                val errorString =
                    "error in ${this@ProfileGetRepositoryImpl::class.java.name}\n" +
                            "message: ${response.body()!!.message}"
                Log.d("Error", errorString)
            }
        }
    }
}