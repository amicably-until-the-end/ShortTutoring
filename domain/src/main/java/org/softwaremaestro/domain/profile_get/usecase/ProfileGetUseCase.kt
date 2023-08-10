package org.softwaremaestro.domain.profile_get.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.profile_get.ProfileGetRepository
import org.softwaremaestro.domain.profile_get.entity.ProfileGetResponseVO
import javax.inject.Inject

class ProfileGetUseCase @Inject constructor(private val profileGetRepository: ProfileGetRepository) {
    suspend fun execute(): Flow<BaseResult<ProfileGetResponseVO, String>> {
        return profileGetRepository.getProfile()
    }
}