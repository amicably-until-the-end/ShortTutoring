package org.softwaremaestro.domain.tutoring_get.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.tutoring_get.TutoringRepository
import org.softwaremaestro.domain.tutoring_get.entity.TutoringVO
import javax.inject.Inject

class TutoringGetUseCase @Inject constructor(private val repository: TutoringRepository) {

    suspend fun execute(): Flow<BaseResult<List<TutoringVO>, String>> {
        return repository.getTutoring()
    }
}