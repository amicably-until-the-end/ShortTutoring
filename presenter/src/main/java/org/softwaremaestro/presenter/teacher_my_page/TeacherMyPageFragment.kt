package org.softwaremaestro.presenter.teacher_my_page

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentTeacherMyPageBinding
import org.softwaremaestro.presenter.student_home.adapter.LectureAdapter
import org.softwaremaestro.presenter.teacher_home.adapter.ReviewAdapter
import org.softwaremaestro.presenter.teacher_my_page.viewmodel.LecturesViewModel
import org.softwaremaestro.presenter.teacher_my_page.viewmodel.MyProfileViewModel
import org.softwaremaestro.presenter.teacher_my_page.viewmodel.ReviewsViewModel

private const val RATING = 4.8334f
private const val NUM_OF_CLIP = 15

@AndroidEntryPoint
class TeacherMyPageFragment : Fragment() {

    private lateinit var binding: FragmentTeacherMyPageBinding

    private val reviewsViewModel: ReviewsViewModel by viewModels()
    private val lecturesViewModel: LecturesViewModel by viewModels()
    private val myProfileViewModel: MyProfileViewModel by viewModels()

    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var lectureAdapter: LectureAdapter

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

        setProfile()

        initReviewRecyclerView()
        initLectureRecyclerView()

        setBtnFollow()
        setTvSettingTimeAndCost()

        setTvReview()
        setTvClip()

        setFollowerMenu()
    }

    private fun setProfile() {

        myProfileViewModel.getMyProfile()

        myProfileViewModel.myProfile.observe(viewLifecycleOwner) {
            with(binding) {
                Glide.with(requireContext()).load(it.profileImage).circleCrop().into(ivTeacherImg)
                tvTeacherUniv.text = "${it.schoolName} ${it.schoolDepartment}"
                tvTeacherName.text = it.name
                tvTeacherRating.text = "%.2f".format(RATING)
                tvNumOfClip.text = NUM_OF_CLIP.toString()
            }
        }

        myProfileViewModel.numOfFollower.observe(viewLifecycleOwner) {
            binding.btnFollow.text = "나를 찜한 학생 $it"
        }
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
                if (it.isEmpty()) View.VISIBLE else View.GONE

            reviewAdapter.setItem(it)
            binding.tvNumOfReview.text = it.size.toString()
        }

        reviewsViewModel.getReviews()
    }

    private fun initLectureRecyclerView() {

        lectureAdapter = LectureAdapter {
            // url을 이용해 영상 재생
            it
        }

        binding.rvClip.apply {
            adapter = lectureAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }

        lecturesViewModel.lectures.observe(requireActivity()) {
            binding.containerClipEmpty.visibility =
                if (it.isEmpty()) View.VISIBLE else View.GONE

            lectureAdapter.setItem(it)
            binding.tvNumOfClip.text = it.size.toString()
        }

        lecturesViewModel.getLectures()
    }

    private fun setBtnFollow() {
        binding.btnFollow.setOnClickListener {
            toggleFollowing()
        }
    }

    private fun toggleFollowing() {
        following = !following

        if (following) {
            myProfileViewModel.addOne()
            binding.btnFollow.setBackgroundResource(R.drawable.bg_radius_5_sub_text_grey)
        } else {
            binding.btnFollow.setBackgroundResource(R.drawable.bg_radius_5_grad_blue)
            myProfileViewModel.minusOne()
        }
    }

    private fun setTvSettingTimeAndCost() {
        binding.containerSettingTimeAndCost.setOnClickListener {
            startActivity(Intent(requireContext(), SettingTimeAndCostActivity::class.java))
        }
    }

    private fun setTvReview() {
        binding.tvReview.setOnClickListener {
            with(binding) {
                containerReview.visibility = View.VISIBLE
                containerClip.visibility = View.GONE

                tvReview.setTextColor(resources.getColor(R.color.black, null))
                tvNumOfReview.setTextColor(resources.getColor(R.color.primary_blue, null))
                tvClip.setTextColor(resources.getColor(R.color.sub_text_grey, null))
                tvNumOfClip.setTextColor(resources.getColor(R.color.sub_text_grey, null))
            }
        }
    }

    private fun setTvClip() {
        binding.tvClip.setOnClickListener {
            with(binding) {
                containerReview.visibility = View.GONE
                containerClip.visibility = View.VISIBLE

                tvClip.setTextColor(resources.getColor(R.color.black, null))
                tvNumOfClip.setTextColor(resources.getColor(R.color.primary_blue, null))
                tvReview.setTextColor(resources.getColor(R.color.sub_text_grey, null))
                tvNumOfReview.setTextColor(resources.getColor(R.color.sub_text_grey, null))
            }
        }
    }

    private fun setFollowerMenu() {
        binding.containerFollower.setOnClickListener {
            startActivity(Intent(requireActivity(), FollowerActivity::class.java))
        }
    }
}
