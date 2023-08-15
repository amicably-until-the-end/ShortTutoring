package org.softwaremaestro.domain.profile_get

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.profile_get.entity.ProfileGetResponseVO

interface ProfileGetRepository {
    suspend fun getProfile(userId: String): Flow<BaseResult<ProfileGetResponseVO, String>>
}