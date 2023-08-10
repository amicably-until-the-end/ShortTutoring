package org.softwaremaestro.data.my_profile_get

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.my_profile_get.model.asDomain
import org.softwaremaestro.data.my_profile_get.remote.MyProfileGetApi
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.my_profile_get.MyProfileGetRepository
import org.softwaremaestro.domain.my_profile_get.entity.MyProfileGetResponseVO
import javax.inject.Inject

class MyProfileGetRepositoryImpl @Inject constructor(private val myProfileGetApi: MyProfileGetApi) :
    MyProfileGetRepository {

    override suspend fun getMyProfile(): Flow<BaseResult<MyProfileGetResponseVO, String>> {
        return flow {
            val response = myProfileGetApi.getMyProfile()
            val body = response.body()!!
            if (body.success == true) {
                body.data?.asDomain()?.let {
                    emit(BaseResult.Success(it))
                }
            } else {
                val errorString =
                    "error in ${this@MyProfileGetRepositoryImpl::class.java.name}\n" +
                            "message: ${response.body()!!.message}"
                Log.d("Error", errorString)
            }
        }
    }
}