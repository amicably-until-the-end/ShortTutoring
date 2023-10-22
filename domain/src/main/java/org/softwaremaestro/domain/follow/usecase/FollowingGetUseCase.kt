package org.softwaremaestro.domain.follow.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.follow.FollowRepository
import org.softwaremaestro.domain.best_teacher_get.entity.TeacherVO
import javax.inject.Inject

class FollowingGetUseCase @Inject constructor(private val followRepository: FollowRepository) {

    suspend fun execute(
        userId: String
    ): Flow<BaseResult<List<TeacherVO>, String>> {
        return followRepository.getFollowing(userId)
    }
}