package org.softwaremaestro.presenter.teacher_my_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentTeacherMyPageBinding
import org.softwaremaestro.presenter.decreaseWidth
import org.softwaremaestro.presenter.increaseWidth
import org.softwaremaestro.presenter.teacher_home.ReviewAdapter
import org.softwaremaestro.presenter.teacher_my_page.viewmodel.NumOfFollowerViewModel
import org.softwaremaestro.presenter.teacher_my_page.viewmodel.ReviewsViewModel


private const val teacherName = "유정연선생님"
private const val teacherSchool = "서울대학교 수의예과"
private const val rating = 4.8895f
private const val numOfClip = 15

private var following = false

class TeacherMyPageFragment : Fragment() {

    private lateinit var binding: FragmentTeacherMyPageBinding

    private val reviewsViewModel: ReviewsViewModel by viewModels()
    private val numOfFollowerViewModel: NumOfFollowerViewModel by viewModels()

    private lateinit var reviewAdapter: ReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTeacherMyPageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setExampleText()

        initReviewRecyclerView()

        binding.btnFollow.setOnClickListener {
            toggleFollowing()
        }

        binding.tvReview.setOnClickListener {
            binding.tvReview.background =
                resources.getDrawable(R.drawable.border_bottom, null)
            binding.tvClip.background = null
            binding.containerReview.visibility = View.VISIBLE
            binding.containerClip.visibility = View.INVISIBLE
        }

        binding.tvClip.setOnClickListener {
            binding.tvReview.background = null
            binding.tvClip.background =
                resources.getDrawable(R.drawable.border_bottom, null)
            binding.containerReview.visibility = View.INVISIBLE
            binding.containerClip.visibility = View.VISIBLE
        }
    }

    private fun toggleFollowing() {
        following = !following

        if (following) {
            binding.btnFollow.setBackgroundResource(R.drawable.btn_corner_radius_10_enabled)
            numOfFollowerViewModel.addOne()

            binding.btnFollow.decreaseWidth(156, 500L, requireContext(),
                onEnd = { binding.btnReserve.visibility = View.VISIBLE }
            )
        } else {
            binding.btnFollow.setBackgroundResource(R.drawable.btn_corner_radius_10_disabled)
            numOfFollowerViewModel.minusOne()

            binding.btnFollow.increaseWidth(156, 500L, requireContext(),
                onStart = { binding.btnReserve.visibility = View.GONE }
            )
        }
    }

    private fun setExampleText() {
        with(binding) {
            tvTeacherName.text = teacherName
            tvTeacherSchool.text = teacherSchool
            tvRating.text = "%.2f".format(rating)
            tvNumOfClip.text = numOfClip.toString()
        }

        numOfFollowerViewModel.numOfFollower.observe(requireActivity()) {
            binding.btnFollow.text = "찜하기 $it"
        }

        numOfFollowerViewModel.getNumOfFollower()
    }

    private fun initReviewRecyclerView() {

        reviewAdapter = ReviewAdapter()

        binding.rvReview.apply {
            adapter = reviewAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }

        reviewsViewModel.reviews.observe(requireActivity()) {
            binding.containerReviewEmpty.visibility =
                if (it.isEmpty()) View.VISIBLE else View.INVISIBLE

            reviewAdapter.setItem(it)
            binding.tvNumOfReview.text = it.size.toString()
        }

        reviewsViewModel.getReviews()
    }
}