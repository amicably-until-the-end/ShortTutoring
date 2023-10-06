package org.softwaremaestro.data.teacher_onlines_get

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.teacher_onlines_get.model.asDomain
import org.softwaremaestro.data.teacher_onlines_get.remote.TeacherOnlinesGetApi
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.teacher_onlines_get.TeacherOnlinesGetRepository
import org.softwaremaestro.domain.teacher_onlines_get.entity.TeacherOnlineVO
import javax.inject.Inject

class TeacherOnlinesGetRepositoryImpl @Inject constructor(private val teacherOnlinesGetApi: TeacherOnlinesGetApi) :
    TeacherOnlinesGetRepository {

    override suspend fun getTeacherOnlines(): Flow<BaseResult<List<TeacherOnlineVO>, String>> {
        return flow {
            val response = teacherOnlinesGetApi.getTeacherOnlines()
            val body = response.body()!!
            if (body.success == true) {
                body.data
                    ?.map { it.asDomain() }
                    ?.let {
                        emit(BaseResult.Success(it))
                    }
            } else {
                val errorString =
                    "error in ${this@TeacherOnlinesGetRepositoryImpl::class.java.name}\n" +
                            "message: ${response.body()!!.message}"
                Log.d("Error", errorString)
            }
        }
    }
}