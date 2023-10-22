package org.softwaremaestro.data.best_teacher_get

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.best_teacher_get.model.asDomain
import org.softwaremaestro.data.best_teacher_get.remote.BestTeacherGetApi
import org.softwaremaestro.domain.best_teacher_get.BestTeacherRepository
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.best_teacher_get.entity.TeacherVO
import javax.inject.Inject

class BestTeacherRepositoryImpl @Inject constructor(private val bestTeacherGetApi: BestTeacherGetApi) :
    BestTeacherRepository {

    override suspend fun getBestTeacher(): Flow<BaseResult<List<TeacherVO>, String>> {
        return flow {
            val response = bestTeacherGetApi.getBestTeacher()
            val body = response.body()
            if (body?.success == true) {
                val teachers = body.data?.map { it.asDomain() } ?: return@flow
                emit(BaseResult.Success(teachers))
            } else {
                val errorString = "error in ${this@BestTeacherRepositoryImpl::class.java.name}\n" +
                        "message : ${body?.message}"
                emit(BaseResult.Error(errorString))
            }
        }
    }
}