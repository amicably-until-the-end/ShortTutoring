package org.softwaremaestro.data.profile

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.profile.model.asDomain
import org.softwaremaestro.data.profile.remote.ProfileApi
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.profile.ProfileRepository
import org.softwaremaestro.domain.profile.entity.MyProfileGetResponseVO
import org.softwaremaestro.domain.profile.entity.ProfileGetResponseVO
import org.softwaremaestro.domain.profile.entity.ProfileUpdateResponseVO
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(private val profileApi: ProfileApi) :
    ProfileRepository {

    override suspend fun getProfile(userId: String): Flow<BaseResult<ProfileGetResponseVO, String>> {
        return flow {
            val response = profileApi.getProfile(userId)
            val body = response.body()!!
            if (body.success == true) {
                body.data?.asDomain()?.let {
                    emit(BaseResult.Success(it))
                }
            } else {
                Log.e(
                    this@ProfileRepositoryImpl::class.java.name,
                    response.body()!!.message.toString()
                )
            }
        }
    }

    override suspend fun updateProfile(): Flow<BaseResult<ProfileUpdateResponseVO, String>> {
        return flow {
            val response = profileApi.updateProfile()
            val body = response.body()!!
            if (body.success == true) {
                body.data?.asDomain()?.let {
                    emit(BaseResult.Success(it))
                }
            } else {
                Log.e(
                    this@ProfileRepositoryImpl::class.java.name,
                    response.body()!!.message.toString()
                )
            }
        }
    }

    override suspend fun getMyProfile(): Flow<BaseResult<MyProfileGetResponseVO, String>> {
        return flow {
            val response = profileApi.getMyProfile()
            val body = response.body()!!
            if (body.success == true) {
                body.data?.asDomain()?.let {
                    emit(BaseResult.Success(it))
                }
            } else {
                val errorString =
                    "error in ${this@ProfileRepositoryImpl::class.java.name}\n" +
                            "message: ${response.body()!!.message}"
                Log.d("Error", errorString)
            }
        }
    }
}