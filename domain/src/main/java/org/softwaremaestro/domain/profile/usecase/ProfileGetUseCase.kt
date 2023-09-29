package org.softwaremaestro.domain.profile.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.profile.ProfileRepository
import org.softwaremaestro.domain.profile.entity.ProfileGetResponseVO
import javax.inject.Inject

class ProfileGetUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    suspend fun execute(userId: String): Flow<BaseResult<ProfileGetResponseVO, String>> {
        return profileRepository.getProfile(userId)
    }
}