package org.softwaremaestro.presenter.teacher_home

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.answer_upload.entity.AnswerUploadVO
import org.softwaremaestro.domain.answer_upload.entity.TeacherVO
import org.softwaremaestro.domain.question_check.entity.QuestionCheckRequestVO
import org.softwaremaestro.presenter.Util.dpToPx
import org.softwaremaestro.presenter.classroom.ClassroomActivity
import org.softwaremaestro.presenter.classroom.SerializedWhiteBoardRoomInfo
import org.softwaremaestro.presenter.databinding.FragmentTeacherHomeBinding
import org.softwaremaestro.presenter.teacher_home.viewmodel.AnswerViewModel
import org.softwaremaestro.presenter.teacher_home.viewmodel.CheckViewModel
import org.softwaremaestro.presenter.teacher_home.viewmodel.QuestionsViewModel

private const val GRIDLAYOUT_SPAN_COUNT = 2
private const val GRIDLAYOUT_SPICING = 8
private const val TEACHER_ID = "test-teacher-id"

@AndroidEntryPoint
class TeacherHomeFragment : Fragment() {

    private lateinit var binding: FragmentTeacherHomeBinding
    private val questionsViewModel: QuestionsViewModel by viewModels()
    private val answerViewModel: AnswerViewModel by viewModels()
    private val checkViewModel: CheckViewModel by viewModels()
    private lateinit var questionAdapter: QuestionAdapter

    //c93f3772-1319-4db7-a88d-4667406a525b


    private lateinit var waitingSnackbar: Snackbar

    //    private lateinit var waitingSnackBar: WaitingSnackbar
    private var selectedRequestId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTeacherHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initWaitingSnackbar()

        initQuestionRecyclerView()

        keepGettingQuestions(1000L)
        keepCheckingQuestionAfterSelect(1000L)

        observe()
    }

    private fun initWaitingSnackbar() {
        waitingSnackbar =
            Snackbar.make(requireView(), "학생의 선택을 확인하고 있습니다.", Snackbar.LENGTH_INDEFINITE).apply {

                val params = (this.view.layoutParams as FrameLayout.LayoutParams).apply {
                    width = FrameLayout.LayoutParams.MATCH_PARENT
                    setMargins(64, 0, 64, 56)
                }

                this.view.layoutParams = params

                setAction("취소하기") {
                    //answer 취소하는 로직 추가
                    waitingSnackbar.dismiss()
                }
            }
    }

    private fun initQuestionRecyclerView() {

        questionAdapter = QuestionAdapter { requestId: String ->
            selectedRequestId = requestId
            waitingSnackbar.show()
            uploadAnswer(requestId)
        }

        binding.rvQuestion.apply {
            adapter = questionAdapter
            layoutManager = GridLayoutManager(requireActivity(), GRIDLAYOUT_SPAN_COUNT)
            setSpacing(GRIDLAYOUT_SPICING)
        }
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
                    0 -> outRect.right = dpToPx(dp, requireContext())
                    // 그리드 레이아웃의 맨 오른쪽 뷰
                    GRIDLAYOUT_SPAN_COUNT - 1 -> outRect.left = dpToPx(dp, requireContext())
                    else -> {
                        outRect.left = dpToPx(dp, requireContext())
                        outRect.right = dpToPx(dp, requireContext())
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
            when (it.status) {
                RequestStatus.SELECTED.noti -> {

                    //Acticity 간 data class 전달을 위해 Serializable 사용
                    val whiteBoardRoomInfo = SerializedWhiteBoardRoomInfo(
                        it.whiteBoardAppId!!,
                        it.whiteBoardUUID!!,
                        it.whiteBoardToken!!,
                        "0"
                    )
                    // 교실 액티비티로 이동한다
                    val intent = Intent(requireActivity(), ClassroomActivity::class.java).apply {
                        putExtra("whiteBoardInfo", whiteBoardRoomInfo)
                    }
                    startActivity(intent)
                }

                RequestStatus.NOT_SELECTED.noti -> {
                    waitingSnackbar.setText("학생이 다른 선생님을 선택했습니다")
                    waitingSnackbar.duration = Snackbar.LENGTH_SHORT
                }
            }
        }
    }

    private fun keepGettingQuestions(timeInterval: Long) {
        viewLifecycleOwner.lifecycleScope.launch {
            while (NonCancellable.isActive) {
                questionsViewModel.getQuestions()
                delay(timeInterval)
            }
        }
    }

    private fun keepCheckingQuestionAfterSelect(timeInterval: Long) {
        viewLifecycleOwner.lifecycleScope.launch {
            while (NonCancellable.isActive) {
                // 학생의 선택을 기다리는 스낵바가 떠있을때
                if (waitingSnackbar.isShown && selectedRequestId != null) {
                    checkViewModel.checkQuestion(
                        selectedRequestId!!,
                        QuestionCheckRequestVO(TEACHER_ID)
                    )
                }
                delay(timeInterval)
            }
        }
    }

    private fun uploadAnswer(requestId: String) {
        answerViewModel.uploadAnswer(
            AnswerUploadVO(
                requestId,
                TeacherVO(TEACHER_ID)
            )
        )
    }
}
