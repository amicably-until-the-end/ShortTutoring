package org.softwaremaestro.domain.profile.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.profile.ProfileRepository
import org.softwaremaestro.domain.profile.entity.MyProfileGetResponseVO
import javax.inject.Inject

class MyProfileGetUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    suspend fun execute(): Flow<BaseResult<MyProfileGetResponseVO, String>> {
        return profileRepository.getMyProfile()
    }
}