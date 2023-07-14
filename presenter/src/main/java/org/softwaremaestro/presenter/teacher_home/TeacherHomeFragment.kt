package org.softwaremaestro.presenter.teacher_home

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ViewUtils.dpToPx
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.answer_upload.entity.AnswerUploadVO
import org.softwaremaestro.domain.answer_upload.entity.TeacherVO
import org.softwaremaestro.domain.question_check.entity.QuestionCheckRequestVO
import org.softwaremaestro.domain.question_get.entity.QuestionGetResultVO
import org.softwaremaestro.presenter.classroom.ClassroomActivity
import org.softwaremaestro.presenter.databinding.FragmentTeacherHomeBinding
import org.softwaremaestro.presenter.teacher_home.viewmodel.AnswerViewModel
import org.softwaremaestro.presenter.teacher_home.viewmodel.CheckViewModel
import org.softwaremaestro.presenter.teacher_home.viewmodel.QuestionsViewModel

private const val GRIDLAYOUT_SPANCOUNT = 2

@AndroidEntryPoint
class TeacherHomeFragment : Fragment() {

    private lateinit var binding: FragmentTeacherHomeBinding
    private val questionsViewModel : QuestionsViewModel by viewModels()
    private val answerViewModel : AnswerViewModel by viewModels()
    private val checkViewModel : CheckViewModel by viewModels()
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var waitingDialog: WaitingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTeacherHomeBinding.inflate(layoutInflater)
        questionAdapter = QuestionAdapter {
            waitingDialog.show()
            uploadAnswer()
        }.apply {
            val items = mutableListOf<QuestionGetResultVO>()
            (0..10).forEach {
                items.add(
                    QuestionGetResultVO(
                        "studentId",
                        null,
                        "고등학교",
                        "수학1",
                        "지수함수와 로그함수",
                        "어려움",
                        "어떻게 풀지 모르겠어요",
                        "not selected",
                        "test-tutoring-id",
                        "today"
                    )
                )
            }
            setItem(items)
        }
        waitingDialog = WaitingDialog(requireActivity())
        keepGettingQuestions(1000L)
        keepCheckingQuestionAfterSelect(1000L)

        binding.rvQuestion.apply {
            adapter = questionAdapter
            layoutManager = GridLayoutManager(requireActivity(), GRIDLAYOUT_SPANCOUNT)
            setSpacing(10)
        }

        observe()

        return binding.root
    }

    private fun RecyclerView.setSpacing(dp: Int) {
        this.addItemDecoration(object: RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                position: Int,
                parent: RecyclerView
            ) {
                super.getItemOffsets(outRect, position, parent)
                when (position % GRIDLAYOUT_SPANCOUNT) {
                    // 그리드 레이아웃의 맨 왼쪽 뷰
                    0 -> outRect.right = dpToPx(dp)
                    // 그리드 레이아웃의 맨 오른쪽 뷰
                    GRIDLAYOUT_SPANCOUNT - 1 -> outRect.right = dpToPx(dp)
                    else -> {
                        outRect.left = dpToPx(dp)
                        outRect.right = dpToPx(dp)
                    }
                }
            }
        })
    }

    private fun observe() {
        observeQuestions()
        observeAnswer()
        observeCheck()
    }

    private fun observeQuestions() {
        questionsViewModel.questions.observe(viewLifecycleOwner) {
            questionAdapter.setItem(it)
            questionAdapter.notifyDataSetChanged()
        }
    }

    private fun observeAnswer() {
        answerViewModel.answer.observe(viewLifecycleOwner) {
            Log.d("answer", it.exampleData)
        }
    }


    private fun observeCheck() {
        checkViewModel.check.observe(viewLifecycleOwner) {
            Log.d("question", it.toString())
            when (it.status) {
                RequestStatus.SELECTED.noti -> {
                    // 교실 액티비티로 이동한다
                    val intent = Intent(requireActivity(), ClassroomActivity::class.java).apply {
                        putExtra("tutoringId", it.tutoringId)
                    }
                    startActivity(intent)
                }
            }
            waitingDialog.dismiss()
        }
    }

    private fun keepGettingQuestions(timeInterval: Long) {
        viewLifecycleOwner.lifecycleScope.launch {
            while (NonCancellable.isActive) {
                if (!waitingDialog.isShowing) {
                    questionsViewModel.getQuestions()
                }
                delay(timeInterval)
            }
        }
    }

    private fun keepCheckingQuestionAfterSelect(timeInterval: Long) {
        viewLifecycleOwner.lifecycleScope.launch {
            while (NonCancellable.isActive) {
                // 학생의 선택을 기다리는 대화상자가 떠있을때
                if (waitingDialog.isShowing) {
                    checkViewModel.checkQuestion("test-request-id", QuestionCheckRequestVO("test-teacher-id"))
                }
                delay(timeInterval)
            }
        }
    }

    private fun uploadAnswer() {
        val problemId = "this should be properly set, or error occurs"
        answerViewModel.uploadAnswer(AnswerUploadVO(problemId, TeacherVO("teacherId")))
    }

    fun dpToPx(dp: Int): Int {
        val metrics = requireContext().resources.displayMetrics;
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), metrics).toInt()
    }
}
