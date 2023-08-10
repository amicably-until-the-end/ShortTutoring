package org.softwaremaestro.presenter.teacher_my_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentTeacherMyPageBinding
import org.softwaremaestro.presenter.Util.decreaseWidth
import org.softwaremaestro.presenter.Util.increaseWidth
import org.softwaremaestro.presenter.teacher_home.ReviewAdapter
import org.softwaremaestro.presenter.teacher_my_page.viewmodel.NumOfFollowerViewModel
import org.softwaremaestro.presenter.teacher_my_page.viewmodel.ProfileViewModel
import org.softwaremaestro.presenter.teacher_my_page.viewmodel.ReviewsViewModel

private const val NUM_OF_CLIP = 15

@AndroidEntryPoint
class TeacherMyPageFragment : Fragment() {

    private lateinit var binding: FragmentTeacherMyPageBinding

    private val reviewsViewModel: ReviewsViewModel by viewModels()
    private val numOfFollowerViewModel: NumOfFollowerViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    private lateinit var reviewAdapter: ReviewAdapter

    private var following = false

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
            binding.btnFollow.setBackgroundResource(R.drawable.btn_corner_radius_10_disabled)
            numOfFollowerViewModel.addOne()

            binding.btnFollow.decreaseWidth(156, 500L, requireContext(),
                onEnd = { binding.btnReserve.visibility = View.VISIBLE }
            )
        } else {
            binding.btnFollow.setBackgroundResource(R.drawable.btn_corner_radius_10_enabled)
            numOfFollowerViewModel.minusOne()

            binding.btnFollow.increaseWidth(156, 500L, requireContext(),
                onStart = { binding.btnReserve.visibility = View.GONE }
            )
        }
    }

    private fun setExampleText() {

        profileViewModel.profile.observe(viewLifecycleOwner) {
            with(binding) {
                Picasso.with(requireContext()).load(it.profileImage).fit().centerCrop()
                    .into(ivTeacherImg)
                tvTeacherSchool.text = "학교 이름 내려주세요"
                tvTeacherName.text = it.name
                tvRating.text = "별점 내려주세요"
                tvNumOfClip.text = NUM_OF_CLIP.toString()
            }
        }

        profileViewModel.getProfile()

        numOfFollowerViewModel.numOfFollower.observe(viewLifecycleOwner) {
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
