package org.softwaremaestro.domain.my_profile_get.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.my_profile_get.MyProfileGetRepository
import org.softwaremaestro.domain.my_profile_get.entity.MyProfileGetResponseVO
import javax.inject.Inject

class MyProfileGetUseCase @Inject constructor(private val myProfileGetRepository: MyProfileGetRepository) {
    suspend fun execute(): Flow<BaseResult<MyProfileGetResponseVO, String>> {
        return myProfileGetRepository.getMyProfile()
    }
}