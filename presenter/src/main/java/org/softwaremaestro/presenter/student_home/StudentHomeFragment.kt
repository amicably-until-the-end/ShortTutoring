package org.softwaremaestro.presenter.student_home

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.following_get.entity.FollowingGetResponseVO
import org.softwaremaestro.domain.lecture_get.entity.LectureVO
import org.softwaremaestro.domain.teacher_get.entity.TeacherVO
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentStudentHomeBinding
import org.softwaremaestro.presenter.question_reserve.QuestionReserveActivity
import org.softwaremaestro.presenter.question_upload.QuestionFormFragment
import org.softwaremaestro.presenter.question_upload.QuestionUploadActivity
import org.softwaremaestro.presenter.student_home.adapter.LectureAdapter
import org.softwaremaestro.presenter.student_home.adapter.TeacherAdapter
import org.softwaremaestro.presenter.student_home.adapter.TeacherFollowingAdapter
import org.softwaremaestro.presenter.student_home.viewmodel.FollowingViewModel
import org.softwaremaestro.presenter.student_home.viewmodel.MyProfileViewModel
import org.softwaremaestro.presenter.student_home.widget.TeacherProfileDialog
import org.softwaremaestro.presenter.util.Util.toPx

private const val GRIDLAYOUT_SPAN_COUNT = 2
private const val GRIDLAYOUT_SPICING = 8

private const val SUBJECT = "수학1"
private const val MAJOR_SECTION_1 = "다항식"
private const val MAJOR_SECTION_2 = "나머지정리"
private const val MAJOR_SECTION_3 = "방정식과 부등식"
private const val PROBLEM_NUMBER_1 = 57
private const val PROBLEM_NUMBER_2 = 33
private const val PROBLEM_NUMBER_3 = 41

@AndroidEntryPoint
class StudentHomeFragment : Fragment() {

    private lateinit var binding: FragmentStudentHomeBinding

    private val followingViewModel: FollowingViewModel by viewModels()
    private val myProfileViewModel: MyProfileViewModel by viewModels()

    private lateinit var teacherFollowingAdapter: TeacherFollowingAdapter
    private lateinit var lectureAdapter: LectureAdapter
    private lateinit var teacherAdapter: TeacherAdapter
    private lateinit var dialogTeacherProfile: BottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentStudentHomeBinding.inflate(layoutInflater)

        myProfileViewModel.getMyProfile()

        initDialogTeacherProfile()

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

    private fun setOthersQuestionRecyclerView() {
        return
    }

    private fun setTeacherFollowingRecyclerView() {
        teacherFollowingAdapter = TeacherFollowingAdapter {
            val dialog = TeacherProfileDialog(it)
            dialog.show(parentFragmentManager, "teacherProfile")
        }

        binding.rvTeacherFollowing.apply {
            adapter = teacherFollowingAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }


    private fun setTeacherRecyclerView() {

        teacherAdapter = TeacherAdapter {
            dialogTeacherProfile.show()
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

    private fun initDialogTeacherProfile() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_teacher_profile, null).apply {
            findViewById<Button>(R.id.btn_follow).setOnClickListener {

            }

            findViewById<LinearLayout>(R.id.container_reserve).setOnClickListener {
                startActivity(Intent(requireActivity(), QuestionReserveActivity::class.java))
            }
        }
        dialogTeacherProfile = BottomSheetDialog(requireContext()).apply {
            setContentView(dialogView)
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
                    "d",
                    "f"
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
                    "d",
                    "f"
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

    private fun RecyclerView.setSpacing(dp: Int) {
        this.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                position: Int,
                parent: RecyclerView
            ) {
                super.getItemOffsets(outRect, position, parent)
                when (position % GRIDLAYOUT_SPAN_COUNT) {
                    // 그리드 레이아웃의 맨 왼쪽 뷰
                    0 -> outRect.right = toPx(dp, requireContext())
                    // 그리드 레이아웃의 맨 오른쪽 뷰
                    GRIDLAYOUT_SPAN_COUNT - 1 -> outRect.left = toPx(dp, requireContext())
                    else -> {
                        outRect.left = toPx(dp, requireContext())
                        outRect.right = toPx(dp, requireContext())
                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                QUESTION_UPLOAD_RESULT -> {
                    val questionId =
                        data?.getStringExtra(QuestionFormFragment.QUESTION_UPLOAD_RESULT)
                    (activity as StudentHomeActivity).apply {

                        moveToChatTab()
                    }
                }
            }
        }
    }

    companion object {
        val QUESTION_UPLOAD_RESULT = 1001
    }
}