package org.softwaremaestro.presenter.teacher_home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.answer_upload.entity.AnswerUploadVO
import org.softwaremaestro.domain.answer_upload.entity.TeacherVO
import org.softwaremaestro.domain.question_check.entity.QuestionCheckRequestVO
import org.softwaremaestro.domain.review_get.ReviewVO
import org.softwaremaestro.presenter.classroom.ClassroomActivity
import org.softwaremaestro.presenter.classroom.item.SerializedWhiteBoardRoomInfo
import org.softwaremaestro.presenter.databinding.FragmentTeacherHomeBinding
import org.softwaremaestro.presenter.teacher_home.viewmodel.AnswerViewModel
import org.softwaremaestro.presenter.teacher_home.viewmodel.CheckViewModel
import org.softwaremaestro.presenter.teacher_home.viewmodel.QuestionsViewModel
import java.text.DecimalFormat

// TODO: 추후 수정
private const val TEACHER_ID = "test-teacher-id"
private const val TEACHER_NAME = "김민수수학"
private const val TEACHER_RATING = 4.8989897f
private const val TEACHER_TEMPERATURE = 48
private const val TEACHER_ANSWER_COST = 2500
private const val REFRESHING_TIME_INTERVAL = 10000L

@AndroidEntryPoint
class TeacherHomeFragment : Fragment() {

    private lateinit var binding: FragmentTeacherHomeBinding
    private val questionsViewModel: QuestionsViewModel by viewModels()
    private val answerViewModel: AnswerViewModel by viewModels()
    private val checkViewModel: CheckViewModel by viewModels()

    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var waitingSnackbar: Snackbar

    private var selectedQuestionId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTeacherHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTexts()

        initWaitingSnackbar()

        initQuestionRecyclerView()

        initReviewRecyclerView()

        keepGettingQuestions(REFRESHING_TIME_INTERVAL)
        keepCheckingQuestionAfterSelect(REFRESHING_TIME_INTERVAL)

        observe()

        // mockup
        setItemToReviewAdapter()
    }

    private fun setTexts() {

        // 선생님 정보
        binding.tvNoti.text = "${TEACHER_NAME} 선생님의 활동이 학생들에게 도움이 되고 있어요!"

        binding.tvRatingAndTemperature.text =
            "현재 별점은 %.1f점, 매너 온도는 %d도에요".format(TEACHER_RATING, TEACHER_TEMPERATURE)

        binding.btnAnswerCost.text = DecimalFormat("###,###").format(TEACHER_ANSWER_COST) + "원"

        // 알림
        binding.tvExampleLikes.text = "조미연 학생이 김민수 선생님을 찜했어요"
        binding.tvExampleReview.text = "이미주 학생이 리뷰를 작성했어요"
    }

    private fun setItemToReviewAdapter() {
        val review = ReviewVO(
            "김민수",
            "2023.7.19",
            "선생님 너무 잘 가르치세요",
            2,
            0,
            null
        )
        reviewAdapter.setItem(
            mutableListOf<ReviewVO>().apply {
                (0..10).forEach {
                    add(review)
                }
            }
        )
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

        questionAdapter = QuestionAdapter { questionId: String ->
            selectedQuestionId = questionId
            waitingSnackbar.show()
            offerTeacher(questionId)
        }

        binding.rvQuestion.apply {
            adapter = questionAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun offerTeacher(requestId: String) {
        answerViewModel.uploadAnswer(
            AnswerUploadVO(
                requestId,
                TeacherVO(TEACHER_ID)
            )
        )
    }

    private fun initReviewRecyclerView() {

        reviewAdapter = ReviewAdapter()

        binding.rvReview.apply {
            adapter = reviewAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun observe() {
        observeQuestions()
        observeAnswer()
        observeCheck()
    }

    private fun observeQuestions() {
        questionsViewModel.questions.observe(viewLifecycleOwner) { questions ->
            questionAdapter.submitList(questions)

            binding.tvNumOfQuestions.text =
                if (questions.isNotEmpty()) "${questions.size}명의 학생이 선생님을 기다리고 있어요"
                else "아직 질문이 올라오지 않았어요"
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
                    //snackbar가 떠 있으면 계속 api를 호출하는 상태임. 결과 나왔으면 그만 호출 해야함.
                    waitingSnackbar.dismiss()
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
                if (waitingSnackbar.isShown && selectedQuestionId != null) {
                    checkViewModel.checkQuestion(
                        selectedQuestionId!!,
                        QuestionCheckRequestVO(TEACHER_ID)
                    )
                }
                delay(timeInterval)
            }
        }
    }
}
