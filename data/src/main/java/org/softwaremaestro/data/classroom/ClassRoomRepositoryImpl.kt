package org.softwaremaestro.data.classroom

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.classroom.model.asDomain
import org.softwaremaestro.data.classroom.remote.ClassRoomApi
import org.softwaremaestro.domain.classroom.ClassRoomRepository
import org.softwaremaestro.domain.classroom.entity.TutoringInfoVO
import org.softwaremaestro.domain.common.BaseResult
import javax.inject.Inject

class ClassRoomRepositoryImpl @Inject constructor(private val classRoomApi: ClassRoomApi) :
    ClassRoomRepository {
    override suspend fun finishClass(tutoringId: String): Flow<BaseResult<String, String>> {
        return flow {
            val result = classRoomApi.finishClassroom(tutoringId)

            if (result.isSuccessful && result.body()?.success == true) {
                emit(BaseResult.Success("Success"))
            } else {
                emit(BaseResult.Error("Error"))
            }
        }
    }

    override suspend fun getTutoringInfo(tutoringId: String): Flow<BaseResult<TutoringInfoVO, String>> {
        return flow {
            val result = classRoomApi.getClassroomInfo(tutoringId)

            if (result.isSuccessful && result.body()?.success == true) {
                emit(BaseResult.Success(result.body()?.data?.asDomain()!!))
            } else {
                emit(BaseResult.Error("Error"))
            }
        }
    }
}