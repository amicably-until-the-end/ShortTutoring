package org.softwaremaestro.domain.profile.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.profile.ProfileRepository
import org.softwaremaestro.domain.profile.entity.MyProfileResVO
import javax.inject.Inject

class ProfileUpdateUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    suspend fun execute(): Flow<BaseResult<MyProfileResVO, String>> {
        return profileRepository.updateProfile()
    }
}