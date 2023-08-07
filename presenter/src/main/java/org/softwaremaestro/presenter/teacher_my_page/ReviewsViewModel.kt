package org.softwaremaestro.presenter.teacher_my_page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.review_get.ReviewVO
import javax.inject.Inject

class ReviewsViewModel @Inject constructor() : ViewModel() {

    private val _reviews: MutableLiveData<List<ReviewVO>> = MutableLiveData()
    val reviews: LiveData<List<ReviewVO>> get() = _reviews

    fun getReviews() {
        viewModelScope.launch {
            val mockUpReviews = mutableListOf<ReviewVO>().apply {
                (0..5).forEach {
                    add(
                        ReviewVO(
                            "김민수",
                            "2023.7.19",
                            "선생님 너무 잘 가르치세요",
                            2,
                            0,
                            null
                        )
                    )
                }
            }

            _reviews.value = mockUpReviews.toList()
        }
    }
}