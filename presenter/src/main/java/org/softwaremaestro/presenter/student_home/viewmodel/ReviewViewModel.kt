package org.softwaremaestro.presenter.student_home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.review.entity.ReviewReqVO
import org.softwaremaestro.domain.review.entity.ReviewResVO
import org.softwaremaestro.domain.review.usecase.ReviewCreateUseCase
import org.softwaremaestro.domain.review.usecase.ReviewGetUseCase
import org.softwaremaestro.presenter.util.Util.logError
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val reviewCreateUseCase: ReviewCreateUseCase,
    private val reviewGetUseCase: ReviewGetUseCase
) :
    ViewModel() {

    private val _reviews = MutableLiveData<List<ReviewResVO>>()
    val reviews: LiveData<List<ReviewResVO>> get() = _reviews

    fun getReviews(teacherId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            reviewGetUseCase.execute(teacherId)
                .catch { exception ->
                    logError(
                        this@ReviewViewModel::class.java,
                        exception.message.toString()
                    )
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> _reviews.postValue(result.data)
                        is BaseResult.Error -> logError(
                            this@ReviewViewModel::class.java,
                            result.toString()
                        )
                    }
                }
        }
    }

    fun createReview(
        tutoringId: String,
        rating: Int,
        comment: String
    ) {
        viewModelScope.launch {

            reviewCreateUseCase.execute(
                ReviewReqVO(
                    tutoringId = tutoringId,
                    rating = rating,
                    comment = comment
                )
            )
                .catch { exception ->
                    logError(this@ReviewViewModel::class.java, exception.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> {}
                        is BaseResult.Error -> logError(
                            this@ReviewViewModel::class.java,
                            result.rawResponse
                        )
                    }
                }
        }
    }
}