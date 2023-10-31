package org.softwaremaestro.presenter.my_page.student_my_page

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.socket.SocketManager
import org.softwaremaestro.domain.tutoring_get.entity.TutoringVO
import org.softwaremaestro.presenter.databinding.FragmentStudentMyPageBinding
import org.softwaremaestro.presenter.login.LoginActivity
import org.softwaremaestro.presenter.login.viewmodel.LoginViewModel
import org.softwaremaestro.presenter.login.viewmodel.WithdrawViewModel
import org.softwaremaestro.presenter.my_page.viewmodel.FollowingViewModel
import org.softwaremaestro.presenter.my_page.viewmodel.ProfileViewModel
import org.softwaremaestro.presenter.student_home.StudentHomeFragment
import org.softwaremaestro.presenter.student_home.adapter.LectureAdapter
import org.softwaremaestro.presenter.student_home.viewmodel.TutoringViewModel
import org.softwaremaestro.presenter.teacher_home.adapter.ReviewAdapter
import org.softwaremaestro.presenter.util.UIState
import org.softwaremaestro.presenter.util.Util
import org.softwaremaestro.presenter.util.widget.ProfileImageSelectBottomDialog
import org.softwaremaestro.presenter.util.widget.SimpleAlertDialog
import org.softwaremaestro.presenter.util.widget.SimpleConfirmDialog
import org.softwaremaestro.presenter.video_player.VideoPlayerActivity

@AndroidEntryPoint
class StudentMyPageFragment : Fragment() {

    private lateinit var binding: FragmentStudentMyPageBinding

    private val followingViewModel: FollowingViewModel by activityViewModels()
    private val tutoringViewModel: TutoringViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private val withdrawViewModel: WithdrawViewModel by viewModels()


    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var lectureAdapter: LectureAdapter

    private lateinit var dialog: ProfileImageSelectBottomDialog
    private lateinit var followings: List<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentMyPageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.getMyProfile()
        SocketManager.userId?.let { followingViewModel.getFollowing(it) }
        initLectureRecyclerView()

        setBtnEditTeacherImg()
        setFollowingMenu()
        setServiceCenterMenu()
        setLogOutContainer()
        setWithdrawMenu()

        observe()
    }

    private fun observe() {
        observeTutoring()
        observeProfile()
        observeFollowing()
        observeWithdrawState()
    }

    private fun observeTutoring() {
        tutoringViewModel.tutoring.observe(requireActivity()) {
            binding.containerClipEmpty.visibility =
                if (it.isEmpty()) View.VISIBLE else View.GONE

            lectureAdapter.apply {
                setItem(it)
                notifyDataSetChanged()
            }

            binding.tvNumOfClip.text = it.size.toString()
        }
    }

    private fun observeProfile() {

        profileViewModel.name.observe(viewLifecycleOwner) {
            binding.tvStudentName.text = it
        }

        profileViewModel.schoolLevel.observe(viewLifecycleOwner) {
            binding.tvStudentSchoolLevel.text = it
        }

        profileViewModel.schoolGrade.observe(viewLifecycleOwner) {
            binding.tvStudentSchoolGrade.text = "${it}학년"
        }

        profileViewModel.image.observe(viewLifecycleOwner) {
            Glide.with(requireContext()).load(it).circleCrop().into(binding.ivStudentImg)
        }

        profileViewModel.followers.observe(viewLifecycleOwner) {
            binding.btnFollow.text = "찜한 선생님 ${it.size}명"
        }
    }

    private fun observeFollowing() {
        followingViewModel.following.observe(viewLifecycleOwner) {
            it ?: return@observe
            followings = it.map { it.teacherId }.filterNotNull()
        }
    }

    private fun observeWithdrawState() {
        withdrawViewModel.withdrawState.observe(viewLifecycleOwner) {
            when (it) {
                is UIState.Success -> {
                    Util.createToast(requireActivity(), "그동안 숏과외를 이용해주셔서 감사합니다").show()
                    val intent = Intent(activity, LoginActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    loginViewModel.clearJWT()
                    startActivity(intent)
                }

                is UIState.Failure -> {
                    Util.createToast(
                        requireActivity(),
                        "오류가 발생했습니다. 잠시 후 다시 시도해주세요."
                    ).show()
                }

                else -> {}
            }

            withdrawViewModel.resetWithdrawState()
        }
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

        tutoringViewModel.getTutoring()
    }

    private fun watchRecordFile(tutoringVO: TutoringVO) {
        val tutoringUrl = tutoringVO.recordFileUrl?.get(0) ?: run {
            SimpleAlertDialog().apply {
                title = "아직 영상이 생성되는 중입니다"
                description = "잠시 후 다시 시도해주세요"
            }.show(parentFragmentManager, "making video")
            return
        }

        val intent = Intent(requireActivity(), VideoPlayerActivity::class.java).apply {
            putExtra(StudentHomeFragment.PROFILE_IMAGE, tutoringVO.opponentProfileImage)
            putExtra(StudentHomeFragment.STUDENT_NAME, tutoringVO.opponentName)
            putExtra(StudentHomeFragment.SCHOOL_LEVEL, tutoringVO.schoolLevel)
            putExtra(StudentHomeFragment.SUBJECT, tutoringVO.schoolSubject)
            putExtra(StudentHomeFragment.DESCRIPTION, tutoringVO.description)
            putExtra(StudentHomeFragment.RECORDING_FILE_URL, tutoringUrl)
            val teacherId = tutoringVO.opponentId
            val following = teacherId in followings
            putExtra(StudentHomeFragment.TEACHER_ID, teacherId)
            putExtra(StudentHomeFragment.FOLLOWING, following)
        }
        startActivity(intent)
    }

    private fun setBtnEditTeacherImg() {
//        binding.containerStudentImg.setOnClickListener {
//            dialog = ProfileImageSelectBottomDialog(
//                onImageChanged = { image ->
//                    binding.ivStudentImg.setBackgroundResource(image)
//                },
//                onSelect = { res ->
//                    val image = BitmapFactory.decodeResource(resources, res).toBase64()
//                    profileViewModel.setImage(image)
////                    profileViewModel.updateProfile()
//                    dialog.dismiss()
//                },
//            )
//            dialog.show(parentFragmentManager, "profileImageSelectBottomDialog")
//        }
    }

    private fun setFollowingMenu() {
        binding.containerFollowing.setOnClickListener {
            startActivity(Intent(requireActivity(), FollowingActivity::class.java))
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

    private fun setWithdrawMenu() {
        binding.containerWithdraw.setOnClickListener {
            SimpleConfirmDialog {
                withdrawViewModel.withdraw()
            }.apply {
                title = "정말 숏과외를 탈퇴할까요?"
                description = "회원을 탈퇴하면 회원 정보가 삭제됩니다"
            }.show(parentFragmentManager, "withdraw")
        }
    }

    companion object {
        private const val SERVICE_CENTER_URL = "http://www.form.short-tutoring.com"
    }
}
