package org.softwaremaestro.data.tutoring_get

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.question_get.model.asDomain
import org.softwaremaestro.data.question_get.remote.QuestionGetApi
import org.softwaremaestro.data.tutoring_get.model.asDomain
import org.softwaremaestro.data.tutoring_get.remote.TutoringGetApi
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_get.QuestionGetRepository
import org.softwaremaestro.domain.question_get.entity.QuestionGetResponseVO
import org.softwaremaestro.domain.tutoring_get.TutoringRepository
import org.softwaremaestro.domain.tutoring_get.entity.TutoringVO
import javax.inject.Inject

class TutoringRepositoryImpl @Inject constructor(private val tutoringGetApi: TutoringGetApi) :
    TutoringRepository {

    override suspend fun getTutoring(): Flow<BaseResult<List<TutoringVO>, String>> {
        return flow {
            val response = tutoringGetApi.getTutoring()
            val body = response.body()!!
            if (body.success == true) {
                val tutoring = body.data?.map { it.asDomain() } ?: return@flow
                emit(BaseResult.Success(tutoring))
            } else {
                val errorString =
                    "error in ${this@TutoringRepositoryImpl::class.java.name}\n" +
                            "message: ${response.body()!!.message}"
                Log.d("Error", errorString)
            }
        }
    }
}