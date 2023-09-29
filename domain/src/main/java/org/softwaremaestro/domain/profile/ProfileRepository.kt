package org.softwaremaestro.domain.profile

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.profile.entity.MyProfileGetResponseVO
import org.softwaremaestro.domain.profile.entity.ProfileGetResponseVO
import org.softwaremaestro.domain.profile.entity.ProfileUpdateResponseVO

interface ProfileRepository {
    suspend fun getProfile(userId: String): Flow<BaseResult<ProfileGetResponseVO, String>>
    suspend fun updateProfile(): Flow<BaseResult<ProfileUpdateResponseVO, String>>
    suspend fun getMyProfile(): Flow<BaseResult<MyProfileGetResponseVO, String>>
}