package org.softwaremaestro.presenter.student_home

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.Util.toPx
import org.softwaremaestro.presenter.databinding.FragmentStudentHomeBinding
import org.softwaremaestro.presenter.question_upload.QuestionUploadActivity
import org.softwaremaestro.presenter.student_home.adapter.BestTeacherAdapter
import org.softwaremaestro.presenter.student_home.adapter.LectureAdapter
import org.softwaremaestro.presenter.student_home.adapter.MyTeacherAdapter
import org.softwaremaestro.presenter.student_home.item.BestTeacher
import org.softwaremaestro.presenter.student_home.item.Lecture
import org.softwaremaestro.presenter.student_home.item.MyTeacher

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

    private lateinit var myTeacherAdapter: MyTeacherAdapter
    private lateinit var lectureAdapter: LectureAdapter
    private lateinit var bestTeacherAdapter: BestTeacherAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentStudentHomeBinding.inflate(layoutInflater)

        initBottomSheetDialog()
        setQuestionButton()
        setMyTeacherRecyclerView()
        setOthersQuestionRecyclerView()
        setLectureRecyclerView()
        setBestTeacherRecyclerView()
        setToolBar()

        // mockup
        setItemToMyTeacherAdapter()
        setItemToBestTeacherAdapter()
        setItemToLectureAdapter()

        return binding.root
    }

    private fun setOthersQuestionRecyclerView() {
        with(binding) {
            btnSubject.text = SUBJECT
            btnMajorSection1.text = MAJOR_SECTION_1
            btnMajorSection2.text = MAJOR_SECTION_2
            btnMajorSection3.text = MAJOR_SECTION_3
            tvNumOfProblems1.text = PROBLEM_NUMBER_1.toString()
            tvNumOfProblems2.text = PROBLEM_NUMBER_2.toString()
            tvNumOfProblems3.text = PROBLEM_NUMBER_3.toString()
        }
    }

    private fun initBottomSheetDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_question_type, null).apply {
            findViewById<Button>(R.id.btn_new_question).setOnClickListener {
                startActivity(Intent(requireContext(), QuestionUploadActivity::class.java))
                bottomSheetDialog.dismiss()
            }
            findViewById<Button>(R.id.btn_re_question).setOnClickListener {
                Toast.makeText(requireContext(), "준비중입니다.", Toast.LENGTH_SHORT).show()
                bottomSheetDialog.dismiss()
            }
        }
        bottomSheetDialog = BottomSheetDialog(requireContext()).apply {
            setContentView(dialogView)
        }
    }


    private fun setMyTeacherRecyclerView() {
        myTeacherAdapter = MyTeacherAdapter {}
        binding.rvMyTeacher.apply {
            adapter = myTeacherAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }


    private fun setBestTeacherRecyclerView() {

        bestTeacherAdapter = BestTeacherAdapter {}

        binding.rvBestTeacher.apply {
            adapter = bestTeacherAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
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
            bottomSheetDialog.show()
        }
    }

    private fun setToolBar() {
        binding.btnToolbarNotification.setOnClickListener {
            findNavController().navigate(R.id.action_studentHomeFragment_to_notificationFragment)
        }
    }

    private fun setItemToMyTeacherAdapter() {
        val teachers = mutableListOf<MyTeacher>().apply {
            add(
                MyTeacher(
                    "수학멘토",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Circle-icons-profile.svg/2048px-Circle-icons-profile.svg.png",
                    "1"
                )
            )
            add(
                MyTeacher(
                    "수학의신",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Circle-icons-profile.svg/2048px-Circle-icons-profile.svg.png",
                    "1"
                ),
            )
            add(
                MyTeacher(
                    "수학의신",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Circle-icons-profile.svg/2048px-Circle-icons-profile.svg.png",
                    "1"
                ),
            )
        }
        myTeacherAdapter.setItem(teachers)
    }

    private fun setItemToBestTeacherAdapter() {
        val lectures = mutableListOf<BestTeacher>().apply {
            add(
                BestTeacher(
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Circle-icons-profile.svg/2048px-Circle-icons-profile.svg.png",
                    "강해린",
                    "1",
                    35,
                    "성균관대학교",
                    4.9f
                )
            )
            (1..4).forEach {
                add(
                    BestTeacher(
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Circle-icons-profile.svg/2048px-Circle-icons-profile.svg.png",
                        "팜하니",
                        "1",
                        31,
                        "피식대학교",
                        4.8f
                    )
                )
            }
        }
        bestTeacherAdapter.setItem(lectures)
    }

    private fun setItemToLectureAdapter() {
        val lectures = mutableListOf<Lecture>().apply {
            add(Lecture("경우의 수를 다 셌는데 안 맞아요", "수학1"))
            add(Lecture("이차곡선의 성질이 이해가 안 돼요", "기하"))
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
}