package org.softwaremaestro.presenter.student_home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.follow.entity.FollowingGetResponseVO
import org.softwaremaestro.domain.lecture_get.entity.LectureVO
import org.softwaremaestro.presenter.coin.ChargeCoinActivity
import org.softwaremaestro.presenter.databinding.FragmentStudentHomeBinding
import org.softwaremaestro.presenter.question_upload.question_normal_upload.QuestionNormalFormFragment
import org.softwaremaestro.presenter.question_upload.question_normal_upload.QuestionUploadActivity
import org.softwaremaestro.presenter.question_upload.question_selected_upload.QuestionReserveActivity
import org.softwaremaestro.presenter.student_home.adapter.LectureAdapter
import org.softwaremaestro.presenter.student_home.adapter.TeacherFollowingAdapter
import org.softwaremaestro.presenter.student_home.adapter.TeacherSimpleAdapter
import org.softwaremaestro.presenter.student_home.viewmodel.FollowingViewModel
import org.softwaremaestro.presenter.student_home.viewmodel.MyProfileViewModel
import org.softwaremaestro.presenter.student_home.widget.TeacherProfileDialog
import org.softwaremaestro.presenter.teacher_profile.TeacherProfileActivity
import org.softwaremaestro.presenter.teacher_profile.viewmodel.FollowUserViewModel
import org.softwaremaestro.presenter.teacher_profile.viewmodel.TeacherViewModel

@AndroidEntryPoint
class StudentHomeFragment : Fragment() {

    private lateinit var binding: FragmentStudentHomeBinding

    private val followingViewModel: FollowingViewModel by viewModels()
    private val followUserViewModel: FollowUserViewModel by viewModels()
    private val myProfileViewModel: MyProfileViewModel by viewModels()
    private val teacherViewModel: TeacherViewModel by viewModels()

    private lateinit var teacherFollowingAdapter: TeacherFollowingAdapter
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

        myProfileViewModel.getMyProfile()
        teacherViewModel.getTeachers()

        initTeacherProfileDialog()

        setQuestionButton()
        setTeacherFollowingRecyclerView()
        setOthersQuestionRecyclerView()
        setLectureRecyclerView()
        setTeacherRecyclerView()
        setNofiBtn()

        observeFollowing()
        observeMyProfile()
        observeTeachers()

        // mockup
        setItemToLectureAdapter()

        return binding.root
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
        teacherFollowingAdapter = TeacherFollowingAdapter {
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
        val sampleData = mutableListOf<FollowingGetResponseVO>().apply {
            add(
                FollowingGetResponseVO(
                    "1",
                    "강해린",
                    "a",
                    "https://yt3.ggpht.com/tWxrapVNxQBe-VnvRxOMAOmyVNsnEmFlT8ON3oQyqLPr6_QwwevRIHsslAmYSNogDehyCQNvqg=s176-c-k-c0x00ffffff-no-nd-rj",
                    "f",
                    "f",
                    "d",
                    "d",
                    1,
                    -1,
                    -1
                )
            )
            add(
                FollowingGetResponseVO(
                    "1",
                    "강해린",
                    "a",
                    "https://yt3.ggpht.com/tWxrapVNxQBe-VnvRxOMAOmyVNsnEmFlT8ON3oQyqLPr6_QwwevRIHsslAmYSNogDehyCQNvqg=s176-c-k-c0x00ffffff-no-nd-rj",
                    "f",
                    "f",
                    "d",
                    "d",
                    1,
                    -1,
                    -1
                )
            )
        }
        teacherFollowingAdapter.setItem(sampleData)

        followingViewModel.following.observe(viewLifecycleOwner) {
            teacherFollowingAdapter.setItem(it)
            teacherFollowingAdapter.notifyDataSetChanged()
        }

        return
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

    private fun setItemToLectureAdapter() {
        val lectures = mutableListOf<LectureVO>().apply {
            add(LectureVO("경우의 수를 다 셌는데 안 맞아요", "수학1", ""))
            add(LectureVO("이차곡선의 성질이 이해가 안 돼요", "기하", ""))
        }
        lectureAdapter.setItem(lectures)
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

    companion object {
        private val QUESTION_UPLOAD_RESULT = 1001
    }
}