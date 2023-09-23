package org.softwaremaestro.presenter.login.univGet

import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.domain.common.BaseResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UnivGetService() {

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit: Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("http://www.career.go.kr/cnet/openapi/")
            .build()

    private val service = retrofit.create(UnivGetApi::class.java)

    suspend fun getUniv(schoolName: String = ""): Flow<BaseResult<List<String>, String>> {
        return flow {
            val response = service.getUniv(
                apiKey = "083d9b778ff00aed07750b3306fa7d37",
                svcType = "api",
                svcCode = "SCHOOL",
                contentType = "json",
                gubun = "univ_list",
                sch1 = 100323,
                searchSchulNm = schoolName
            )
            if (response.isSuccessful) {
                emit(BaseResult.Success(response.body()!!.dataSearch.content.map { it.schoolName }))
            } else {
                emit(BaseResult.Error(response.message()))
            }
        }
    }
}