package org.softwaremaestro.presenter.my_page.teacher_my_page

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.socket.SocketManager
import org.softwaremaestro.domain.tutoring_get.entity.TutoringVO
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentTeacherMyPageBinding
import org.softwaremaestro.presenter.login.LoginActivity
import org.softwaremaestro.presenter.login.viewmodel.LoginViewModel
import org.softwaremaestro.presenter.my_page.viewmodel.FollowerViewModel
import org.softwaremaestro.presenter.my_page.viewmodel.ProfileViewModel
import org.softwaremaestro.presenter.student_home.StudentHomeFragment
import org.softwaremaestro.presenter.student_home.adapter.LectureAdapter
import org.softwaremaestro.presenter.student_home.viewmodel.ReviewViewModel
import org.softwaremaestro.presenter.student_home.viewmodel.TutoringViewModel
import org.softwaremaestro.presenter.teacher_home.adapter.ReviewAdapter
import org.softwaremaestro.presenter.util.toRating
import org.softwaremaestro.presenter.util.widget.ProfileImageSelectBottomDialog
import org.softwaremaestro.presenter.video_player.VideoPlayerActivity

@AndroidEntryPoint
class TeacherMyPageFragment : Fragment() {

    private lateinit var binding: FragmentTeacherMyPageBinding

    private val reviewViewModel: ReviewViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val followerViewModel: FollowerViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private val tutoringViewModel: TutoringViewModel by viewModels()


    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var lectureAdapter: LectureAdapter

    private lateinit var dialog: ProfileImageSelectBottomDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeacherMyPageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getRemoteData()
        initReviewRecyclerView()
        initLectureRecyclerView()
        setBtnEditTeacherImg()
        setTvSettingTimeAndCost()
        setTvReview()
        setTvClip()
        setFollowerMenu()
        setServiceCenterMenu()
        setLogOutContainer()
        observe()
    }

    private fun getRemoteData() {
        profileViewModel.getMyProfile()
        SocketManager.userId?.let { reviewViewModel.getReviews(it) }
        tutoringViewModel.getTutoring()
    }

    private fun observe() {
        observeProfile()
        observeReview()
        observeTutoring()
    }

    private fun observeReview() {
        reviewViewModel.reviews.observe(requireActivity()) { reviews ->
            reviews ?: return@observe
            val reviewsNotEmpty =
                reviews.filter { it.reviewComment != null && it.reviewComment!!.length >= 3 }

            binding.containerReviewEmpty.visibility =
                if (reviewsNotEmpty.isEmpty()) View.VISIBLE else View.GONE

            binding.tvNumOfReview.text = reviewsNotEmpty.size.toString()
            reviewAdapter.setItem(reviewsNotEmpty)
            reviewAdapter.notifyDataSetChanged()
            binding.tvNumOfReview.text = reviewsNotEmpty.size.toString()
        }
    }

    private fun observeTutoring() {
        tutoringViewModel.tutoring.observe(requireActivity()) {
            binding.containerClipEmpty.visibility =
                if (it.isEmpty()) View.VISIBLE else View.GONE

            lectureAdapter.setItem(it)
            lectureAdapter.notifyDataSetChanged()
            binding.tvNumOfClip.text = it.size.toString()
        }
    }

    private fun setBtnEditTeacherImg() {
//        binding.containerTeacherImg.setOnClickListener {
//            dialog = ProfileImageSelectBottomDialog(
//                onImageChanged = { image ->
//                    binding.ivTeacherImg.setBackgroundResource(image)
//                },
//                onSelect = { res ->
//                    val image = BitmapFactory.decodeResource(resources, res).toBase64()
//                    profileViewModel.setImage(image)
////                    profileViewModel.updateProfile()
//
//                    dialog.dismiss()
//                },
//            )
//            dialog.show(parentFragmentManager, "profileImageSelectBottomDialog")
//        }
    }

    private fun observeProfile() {

        profileViewModel.name.observe(viewLifecycleOwner) {
            binding.tvTeacherName.text = it
        }

        profileViewModel.bio.observe(viewLifecycleOwner) {
            binding.tvTeacherBio.text = it
        }

        profileViewModel.univName.observe(viewLifecycleOwner) {
            binding.tvTeacherUniv.text = it
        }

        profileViewModel.major.observe(viewLifecycleOwner) {
            binding.tvTeacherMajor.text = it
        }

        profileViewModel.rating.observe(viewLifecycleOwner) {
            binding.tvTeacherRating.text = it.toRating()
        }

        profileViewModel.image.observe(viewLifecycleOwner) {
            Glide.with(requireContext()).load(it).circleCrop().into(binding.ivTeacherImg)
        }

        profileViewModel.followers.observe(viewLifecycleOwner) {
            binding.btnFollow.text = "찜한 학생 ${it.size}명"
        }
    }

    private fun initReviewRecyclerView() {

        reviewAdapter = ReviewAdapter()

        binding.rvReview.apply {
            adapter = reviewAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }

        SocketManager.userId?.let { reviewViewModel.getReviews(it) }
    }

    private fun initLectureRecyclerView() {

        lectureAdapter = LectureAdapter {
            watchRecordFile(it)
        }

        binding.rvClip.apply {
            adapter = lectureAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun watchRecordFile(tutoringVO: TutoringVO) {
        val intent = Intent(requireActivity(), VideoPlayerActivity::class.java).apply {
            putExtra(StudentHomeFragment.PROFILE_IMAGE, tutoringVO.opponentProfileImage)
            putExtra(StudentHomeFragment.STUDENT_NAME, tutoringVO.opponentName)
            putExtra(StudentHomeFragment.SCHOOL_LEVEL, tutoringVO.schoolLevel)
            putExtra(StudentHomeFragment.SUBJECT, tutoringVO.schoolSubject)
            putExtra(StudentHomeFragment.DESCRIPTION, tutoringVO.description)
            tutoringVO.recordFileUrl?.get(0)
                ?.let { putExtra(StudentHomeFragment.RECORDING_FILE_URL, it) }
        }
        startActivity(intent)
    }

    private fun setTvSettingTimeAndCost() {
//        binding.containerSettingTimeAndCost.setOnClickListener {
//            startActivity(Intent(requireContext(), SettingTimeAndCostActivity::class.java))
//        }
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

    private fun setServiceCenterMenu() {
        binding.containerServiceCenter.setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                addCategory(Intent.CATEGORY_BROWSABLE)
                data = Uri.parse(SERVICE_CENTER_URL)
            }
            startActivity(intent)
        }
    }

    private fun setLogOutContainer() {
        binding.containerLogOut.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            loginViewModel.clearJWT()
            startActivity(intent)
        }
    }

    companion object {
        private const val SERVICE_CENTER_URL = "http://form.short-tutoring.com"
    }
}
