package org.softwaremaestro.domain.my_profile_get

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.my_profile_get.entity.MyProfileGetResponseVO

interface MyProfileGetRepository {
    suspend fun getMyProfile(): Flow<BaseResult<MyProfileGetResponseVO, String>>
}