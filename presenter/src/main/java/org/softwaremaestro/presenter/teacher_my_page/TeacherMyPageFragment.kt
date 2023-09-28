package org.softwaremaestro.presenter.teacher_my_page

import android.content.Intent
import android.graphics.BitmapFactory
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
import org.softwaremaestro.presenter.teacher_my_page.viewmodel.FollowerViewModel
import org.softwaremaestro.presenter.teacher_my_page.viewmodel.LecturesViewModel
import org.softwaremaestro.presenter.teacher_my_page.viewmodel.MyProfileViewModel
import org.softwaremaestro.presenter.teacher_my_page.viewmodel.ReviewsViewModel
import org.softwaremaestro.presenter.util.toBase64
import org.softwaremaestro.presenter.util.widget.ProfileImageSelectBottomDialog

private const val RATING = 4.8334f
private const val NUM_OF_CLIP = 15

@AndroidEntryPoint
class TeacherMyPageFragment : Fragment() {

    private lateinit var binding: FragmentTeacherMyPageBinding

    private val reviewsViewModel: ReviewsViewModel by viewModels()
    private val lecturesViewModel: LecturesViewModel by viewModels()
    private val myProfileViewModel: MyProfileViewModel by viewModels()
    private val followerViewModel: FollowerViewModel by viewModels()

    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var lectureAdapter: LectureAdapter

    private lateinit var dialog: ProfileImageSelectBottomDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTeacherMyPageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myProfileViewModel.getMyProfile()
        observe()

        initReviewRecyclerView()
        initLectureRecyclerView()

        setBtnEditTeacherImg()
        setTvSettingTimeAndCost()

        setTvReview()
        setTvClip()

        setFollowerMenu()
    }

    private fun observe() {
        observeProfile()
        observeFollower()
        observeImage()
    }

    private fun observeFollower() {
        followerViewModel.follower.observe(viewLifecycleOwner) {
            binding.btnFollow.text = "찜한 사람 ${it.size}명"
        }
    }

    private fun observeImage() {
        myProfileViewModel.image.observe(viewLifecycleOwner) {
            Glide.with(requireContext()).load(it)
                .centerCrop()
                .into(binding.ivTeacherImg)

            // Todo: 프로필 변경 Api 호출
        }
    }

    private fun setBtnEditTeacherImg() {
        binding.containerTeacherImg.setOnClickListener {
            dialog = ProfileImageSelectBottomDialog(
                onImageChanged = { image ->
                    binding.ivTeacherImg.setBackgroundResource(image)
                },
                onSelect = { res ->
                    val image = BitmapFactory.decodeResource(resources, res).toBase64()
                    myProfileViewModel.setImage(image)

                    dialog.dismiss()
                },
            )
            dialog.show(parentFragmentManager, "profileImageSelectBottomDialog")
        }
    }

    private fun observeProfile() {

        myProfileViewModel.id.observe(viewLifecycleOwner) {
            followerViewModel.getFollower(it)
        }

        myProfileViewModel.name.observe(viewLifecycleOwner) {
            binding.tvTeacherName.text = it
        }

        myProfileViewModel.bio.observe(viewLifecycleOwner) {
            binding.tvTeacherBio.text = it
        }

        myProfileViewModel.univName.observe(viewLifecycleOwner) {
            binding.tvTeacherUniv.text = it
        }

        myProfileViewModel.major.observe(viewLifecycleOwner) {
            binding.tvTeacherMajor.text = it
        }

//        myProfileViewModel.rating.observe(viewLifecycleOwner) {
//            binding.tvTeacherRating.text = it
//        }

        myProfileViewModel.image.observe(viewLifecycleOwner) {
            Glide.with(requireContext()).load(it).circleCrop().into(binding.ivTeacherImg)
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
