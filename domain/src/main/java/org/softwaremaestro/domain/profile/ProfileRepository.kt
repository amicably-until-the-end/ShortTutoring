package org.softwaremaestro.domain.profile

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.profile.entity.MyProfileResVO
import org.softwaremaestro.domain.profile.entity.ProfileResVO

interface ProfileRepository {
    suspend fun getProfile(userId: String): Flow<BaseResult<ProfileResVO, String>>
    suspend fun updateProfile(): Flow<BaseResult<MyProfileResVO, String>>
    suspend fun getMyProfile(): Flow<BaseResult<MyProfileResVO, String>>
}