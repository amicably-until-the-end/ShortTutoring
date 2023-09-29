package org.softwaremaestro.presenter.student_home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.follow.entity.FollowingGetResponseVO
import org.softwaremaestro.domain.lecture_get.entity.LectureVO
import org.softwaremaestro.domain.teacher_get.entity.TeacherVO
import org.softwaremaestro.presenter.R
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

@AndroidEntryPoint
class StudentHomeFragment : Fragment() {

    private lateinit var binding: FragmentStudentHomeBinding

    private val followingViewModel: FollowingViewModel by viewModels()
    private val myProfileViewModel: MyProfileViewModel by viewModels()

    private lateinit var teacherFollowingAdapter: TeacherFollowingAdapter
    private lateinit var lectureAdapter: LectureAdapter
    private lateinit var teacherAdapter: TeacherSimpleAdapter
    private lateinit var dialogTeacherProfile: TeacherProfileDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentStudentHomeBinding.inflate(layoutInflater)

        myProfileViewModel.getMyProfile()

        initTeacherProfileDialog()

        setQuestionButton()
        setTeacherFollowingRecyclerView()
        setOthersQuestionRecyclerView()
        setLectureRecyclerView()
        setTeacherRecyclerView()
        setToolBar()

        observeFollowing()
        observeMyProfile()

        // mockup
        setItemToBestTeacherAdapter()
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
            onFollowBtnClicked = {},
            onReserveBtnClicked = {
                startActivityForResult(
                    Intent(
                        requireActivity(),
                        QuestionReserveActivity::class.java
                    ), QUESTION_UPLOAD_RESULT
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
            val teacherVO = TeacherVO(
                profileUrl = it.profileImage,
                nickname = it.name,
                teacherId = it.id,
                bio = it.bio,
                pickCount = -1,
                univ = "${it.schoolName} ${it.schoolDepartment}",
                rating = -1.0f
            )

            dialogTeacherProfile.item = teacherVO
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
            dialogTeacherProfile.item = teacherVO
            dialogTeacherProfile.show(parentFragmentManager, "teacherProfile")
        }

        binding.rvTeacher.apply {
            adapter = teacherAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
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

    private fun setToolBar() {
        binding.btnToolbarNotification.setOnClickListener {
            findNavController().navigate(R.id.action_studentHomeFragment_to_notificationFragment)
        }
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

            if (it.id == null) {
                // 에러 처리
            } else {
                followingViewModel.getFollowing(it.id!!)
            }
        }
    }

    private fun setItemToBestTeacherAdapter() {

        val teachers = mutableListOf<TeacherVO>().apply {
            add(
                TeacherVO(
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Circle-icons-profile.svg/2048px-Circle-icons-profile.svg.png",
                    "강해린",
                    "1",
                    "풀 수 없는 문제는 없다.",
                    35,
                    "성균관대학교",
                    4.9f
                )
            )
            (1..4).forEach {
                add(
                    TeacherVO(
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Circle-icons-profile.svg/2048px-Circle-icons-profile.svg.png",
                        "팜하니",
                        "1",
                        "풀 수 없는 문제는 없다.",
                        31,
                        "피식대학교",
                        4.8f
                    )
                )
            }
        }
        teacherAdapter.setItem(teachers)
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

    companion object {
        private val QUESTION_UPLOAD_RESULT = 1001
    }
}