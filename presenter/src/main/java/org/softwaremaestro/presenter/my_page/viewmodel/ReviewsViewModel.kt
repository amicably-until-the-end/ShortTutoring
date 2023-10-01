package org.softwaremaestro.presenter.my_page.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.review_get.ReviewVO
import javax.inject.Inject

@HiltViewModel
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