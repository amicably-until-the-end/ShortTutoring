package org.softwaremaestro.presenter.student_home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.follow.entity.FollowingGetResponseVO
import org.softwaremaestro.domain.socket.SocketManager
import org.softwaremaestro.domain.teacher_get.entity.TeacherVO
import org.softwaremaestro.presenter.coin.ChargeCoinActivity
import org.softwaremaestro.presenter.databinding.FragmentStudentHomeBinding
import org.softwaremaestro.presenter.question_upload.question_normal_upload.QuestionNormalFormFragment
import org.softwaremaestro.presenter.question_upload.question_normal_upload.QuestionUploadActivity
import org.softwaremaestro.presenter.question_upload.question_selected_upload.QuestionReserveActivity
import org.softwaremaestro.presenter.student_home.adapter.LectureAdapter
import org.softwaremaestro.presenter.student_home.adapter.TeacherCircularAdapter
import org.softwaremaestro.presenter.student_home.adapter.TeacherSimpleAdapter
import org.softwaremaestro.presenter.student_home.viewmodel.FollowingViewModel
import org.softwaremaestro.presenter.student_home.viewmodel.LectureViewModel
import org.softwaremaestro.presenter.student_home.viewmodel.MyProfileViewModel
import org.softwaremaestro.presenter.student_home.viewmodel.TeacherOnlineViewModel
import org.softwaremaestro.presenter.student_home.widget.TeacherProfileDialog
import org.softwaremaestro.presenter.teacher_profile.TeacherProfileActivity
import org.softwaremaestro.presenter.teacher_profile.viewmodel.FollowUserViewModel
import org.softwaremaestro.presenter.teacher_profile.viewmodel.TeacherViewModel

@AndroidEntryPoint
class StudentHomeFragment : Fragment() {

    private lateinit var binding: FragmentStudentHomeBinding

    private val followingViewModel: FollowingViewModel by activityViewModels()
    private val teacherOnlineViewModel: TeacherOnlineViewModel by activityViewModels()
    private val followUserViewModel: FollowUserViewModel by activityViewModels()
    private val myProfileViewModel: MyProfileViewModel by activityViewModels()
    private val teacherViewModel: TeacherViewModel by activityViewModels()
    private val lectureViewModel: LectureViewModel by activityViewModels()

    private lateinit var teacherFollowingAdapter: TeacherCircularAdapter
    private lateinit var teacherOnlineAdapter: TeacherCircularAdapter
    private lateinit var lectureAdapter: LectureAdapter
    private lateinit var teacherAdapter: TeacherSimpleAdapter
    private lateinit var dialogTeacherProfile: TeacherProfileDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentStudentHomeBinding.inflate(layoutInflater)

        // Todo: 나중에 api로 받아와야 함
        binding.cbCoin.coin = 1350
        binding.cbCoin.setOnClickListener {
            startActivity(Intent(requireContext(), ChargeCoinActivity::class.java))
        }
        getRemoteData()
        initTeacherProfileDialog()
        setQuestionButton()
        setTeacherFollowingRecyclerView()
        setTeacherOnlineRecyclerView()
        setOthersQuestionRecyclerView()
        setLectureRecyclerView()
        setTeacherRecyclerView()
        setNofiBtn()
        setObserver()
        return binding.root
    }

    private fun getRemoteData() {
        myProfileViewModel.getMyProfile()
        teacherViewModel.getTeachers()
        lectureViewModel.getLectures()
        SocketManager.userId?.let { followingViewModel.getFollowing(it) }
        teacherOnlineViewModel.getTeacherOnlines()
    }

    private fun initTeacherProfileDialog() {
        dialogTeacherProfile = TeacherProfileDialog(
            onProfileClicked = { teacherId ->
                startActivityForResult(
                    Intent(
                        requireActivity(),
                        TeacherProfileActivity::class.java
                    ).apply {
                        putExtra("teacher-id", teacherId)
                    }, QUESTION_UPLOAD_RESULT
                )

                dialogTeacherProfile.dismiss()
            },
            onFollowBtnClicked = { following, teacherId ->
                if (following) {
                    followUserViewModel.unfollowUser(teacherId)
                    Toast.makeText(requireContext(), "선생님 찜하기가 해제되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    followUserViewModel.followUser(teacherId)
                    Toast.makeText(requireContext(), "선생님을 찜했습니다", Toast.LENGTH_SHORT).show()
                }
                // teacher의 followers를 갱신하기 위해 getTeachers() 호출
                teacherViewModel.getTeachers()
            },
            onReserveBtnClicked = { teacherId ->
                startActivityForResult(
                    Intent(
                        requireActivity(),
                        QuestionReserveActivity::class.java
                    ).apply {
                        putExtra("teacher-id", teacherId)
                    }, QUESTION_UPLOAD_RESULT
                )

                dialogTeacherProfile.dismiss()
            }
        )
    }

    private fun setOthersQuestionRecyclerView() {
        return
    }

    private fun setTeacherFollowingRecyclerView() {
        teacherFollowingAdapter = TeacherCircularAdapter {
//            val teacherVO = TeacherVO(
//                profileUrl = it.profileImage,
//                nickname = it.name,
//                teacherId = it.id,
//                bio = it.bio,
//                pickCount = -1,
//                univ = "${it.schoolName} ${it.schoolDepartment}",
//                rating = -1.0f
//            )
//
//            dialogTeacherProfile.item = teacherVO
            dialogTeacherProfile.show(parentFragmentManager, "teacherProfile")
        }

        binding.rvTeacherFollowing.apply {
            adapter = teacherFollowingAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setTeacherOnlineRecyclerView() {
        teacherOnlineAdapter = TeacherCircularAdapter {
            val teacherVO = TeacherVO(
                profileUrl = it.profileImage,
                nickname = it.name,
                teacherId = it.id,
                bio = it.bio,
                univ = "${it.schoolName} ${it.schoolDepartment}",
                rating = -1.0f,
                listOf(),
                -1,
            )

            dialogTeacherProfile.setItem(teacherVO)
            dialogTeacherProfile.show(parentFragmentManager, "teacherProfile")
        }

        binding.rvTeacherOnline.apply {
            adapter = teacherOnlineAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }


    private fun setTeacherRecyclerView() {

        teacherAdapter = TeacherSimpleAdapter { teacherVO ->
            dialogTeacherProfile.setItem(teacherVO)
            dialogTeacherProfile.show(parentFragmentManager, "teacherProfile")
        }

        binding.rvTeacher.apply {
            adapter = teacherAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setNofiBtn() {
        binding.btnToolbarNotification.setOnClickListener {
            showNoti("제목", "본문") {
                // when confirm clicked
            }
        }
    }

    private fun setLectureRecyclerView() {

        lectureAdapter = LectureAdapter {}

        binding.rvLecture.apply {
            adapter = lectureAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setQuestionButton() {
        binding.btnQuestion.setOnClickListener {
            startQuestionUploadActivity()
        }
    }

    private fun startQuestionUploadActivity() {
        val intent = Intent(requireContext(), QuestionUploadActivity::class.java)
        startActivityForResult(intent, QUESTION_UPLOAD_RESULT)
    }

    private fun observeFollowing() {
        followingViewModel.following.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.containerMyTeacherSection.visibility = View.VISIBLE
                binding.nsTeacherFollowing.visibility = View.VISIBLE
            } else {
                binding.dvTeacher.visibility = View.GONE
                binding.nsTeacherFollowing.visibility = View.GONE
                if (teacherOnlineViewModel.teacherOnlines.value.isNullOrEmpty()) {
                    binding.containerMyTeacherSection.visibility = View.GONE
                } else {
                    binding.containerMyTeacherSection.visibility = View.VISIBLE
                }
            }
            teacherFollowingAdapter.setItem(
                if (teacherOnlineViewModel.teacherOnlines.value.isNullOrEmpty()) it
                else it.take(3)
            )
            teacherFollowingAdapter.notifyDataSetChanged()
        }
    }

    private fun observeMyProfile() {
        myProfileViewModel.myProfile.observe(viewLifecycleOwner) {

            // Todo: 코인 설정
        }
    }

    private fun observeTeachers() {
        teacherViewModel.teachers.observe(viewLifecycleOwner) {
            teacherAdapter.setItem(it)
            teacherAdapter.notifyDataSetChanged()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                QUESTION_UPLOAD_RESULT -> {
                    val questionId =
                        data?.getStringExtra(QuestionNormalFormFragment.QUESTION_UPLOAD_RESULT)
                    (activity as StudentHomeActivity).apply {
                        moveToChatTab()
                    }
                }
            }
        }
    }

    fun showNoti(title: String, description: String, onConfirmClick: () -> Unit) {
        binding.nwNoti.apply {
            visibility = View.VISIBLE
            setTitle(title)
            setDescription(description)
            setOnConfirmClick {
                onConfirmClick()
            }
        }
    }

    private fun setObserver() {
        observeFollowing()
        observeMyProfile()
        observeTeachers()
        observeLectures()
        observeTeacherOnlines()
    }

    private fun observeLectures() {
        lectureViewModel.lectures.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.tvLectureDesc.text = "지난 수업을 복습해봐요"
                binding.rvLecture.visibility = View.VISIBLE
                binding.containerLectureEmpty.visibility = View.GONE
            } else {
                binding.tvLectureDesc.text = "숏과외에 처음 오신 것을 환영해요!"
                binding.rvLecture.visibility = View.GONE
                binding.containerLectureEmpty.visibility = View.VISIBLE
            }
            lectureAdapter.setItem(it)
            lectureAdapter.notifyDataSetChanged()
        }
    }

    private fun observeTeacherOnlines() {
        teacherOnlineViewModel.teacherOnlines.observe(viewLifecycleOwner) { teacherOnlines ->
            teacherOnlines.map {
                FollowingGetResponseVO(
                    id = it.teacherId,
                    name = it.nickname,
                    bio = it.bio,
                    profileImage = it.profileUrl,
                    role = "teacher",
                    schoolDivision = "",
                    schoolName = it.univ,
                    schoolDepartment = "더미 학과",
                    schoolGrade = -1,
                    followersCount = it.followers?.size,
                    followingCount = -1
                )
            }.let {
                teacherOnlineAdapter.setItem(it)

                if (it.isNotEmpty()) {
                    binding.containerMyTeacherSection.visibility = View.VISIBLE
                    binding.nsTeacherOnline.visibility = View.VISIBLE
                } else {
                    binding.dvTeacher.visibility = View.GONE
                    binding.nsTeacherOnline.visibility = View.GONE
                    if (followingViewModel.following.value.isNullOrEmpty()) {
                        binding.containerMyTeacherSection.visibility = View.GONE
                    } else {
                        binding.containerMyTeacherSection.visibility = View.VISIBLE
                    }
                }
            }
            teacherOnlineAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        private val QUESTION_UPLOAD_RESULT = 1001
    }
}