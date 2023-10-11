package org.softwaremaestro.presenter.teacher_home

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.event.entity.EventVO
import org.softwaremaestro.domain.event.entity.EventsVO
import org.softwaremaestro.domain.question_get.entity.QuestionGetResponseVO
import org.softwaremaestro.domain.socket.SocketManager
import org.softwaremaestro.presenter.databinding.FragmentTeacherHomeBinding
import org.softwaremaestro.presenter.student_home.adapter.EventAdapter
import org.softwaremaestro.presenter.student_home.viewmodel.HomeViewModel
import org.softwaremaestro.presenter.student_home.viewmodel.MyProfileViewModel
import org.softwaremaestro.presenter.teacher_home.QuestionDetailActivity.Companion.CHAT_ID
import org.softwaremaestro.presenter.teacher_home.QuestionDetailActivity.Companion.OFFER_RESULT
import org.softwaremaestro.presenter.teacher_home.QuestionDetailActivity.Companion.OFFER_SUCCESS
import org.softwaremaestro.presenter.teacher_home.adapter.QuestionAdapter
import org.softwaremaestro.presenter.teacher_home.adapter.ReviewAdapter
import org.softwaremaestro.presenter.teacher_home.viewmodel.AnswerViewModel
import org.softwaremaestro.presenter.teacher_home.viewmodel.CheckViewModel
import org.softwaremaestro.presenter.teacher_home.viewmodel.OfferRemoveViewModel
import org.softwaremaestro.presenter.teacher_home.viewmodel.QuestionsViewModel

private const val REFRESHING_TIME_INTERVAL = 10000L

@AndroidEntryPoint
class TeacherHomeFragment : Fragment() {

    private lateinit var binding: FragmentTeacherHomeBinding
    private val questionsViewModel: QuestionsViewModel by viewModels()
    private val myProfileViewModel: MyProfileViewModel by viewModels()
    private val answerViewModel: AnswerViewModel by viewModels()
    private val offerRemoveViewModel: OfferRemoveViewModel by viewModels()
    private val checkViewModel: CheckViewModel by viewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var requestActivity: ActivityResultLauncher<Intent>

    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var eventAdapter: EventAdapter
    private lateinit var waitingSnackbar: Snackbar
    private var isCalledFirstTime = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTeacherHomeBinding.inflate(layoutInflater)
        registerOfferResult()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myProfileViewModel.getMyProfile()

        setTexts()

        initWaitingSnackbar()
        initQuestionRecyclerView()
        initReviewRecyclerView()
        initEventAdapter()

        keepGettingQuestions(REFRESHING_TIME_INTERVAL)

        observe()
    }

    private fun setTexts() {


//        binding.tvRatingAndTemperature.text =
//            "현재 별점은 %.1f점, 매너 온도는 %d도에요".format(TEACHER_RATING, TEACHER_TEMPERATURE)
//
//        binding.btnAnswerCost.text = DecimalFormat("###,###").format(TEACHER_ANSWER_COST) + "원"


    }

    private fun registerOfferResult() {
        requestActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK && it.data?.getIntExtra(
                        OFFER_RESULT,
                        0
                    ) == OFFER_SUCCESS
                ) {
                    Toast.makeText(requireActivity(), "수업을 제안했습니다.", Toast.LENGTH_SHORT)
                        .show()
                    val chatId = it.data?.getStringExtra(CHAT_ID)
                    (activity as TeacherHomeActivity).moveToChatTab(chatId)
                } else {
                    Toast.makeText(requireActivity(), "수업 제안에 실패했습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
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

    private fun startQuestionDetailActivity(
        question: QuestionGetResponseVO,
        hopeTime: String?
    ) {


        val intent = Intent(requireActivity(), QuestionDetailActivity::class.java).apply {
            putStringArrayListExtra(IMAGE, question.images as ArrayList<String>)
            putExtra(SUBJECT, question.problemSubject)
            putExtra(DESCRIPTION, question.problemDescription)
            putExtra(QUESTION_ID, question.id)
            putExtra(
                HOPE_TIME,
                if (!hopeTime.isNullOrEmpty()) hopeTime else "시간을 선택하지 않았어요."
            )
            if (SocketManager.userId != null && question.offerTeachers != null) {
                putExtra(OFFERED_ALREADY, SocketManager.userId!! in question.offerTeachers!!)
            }
        }
        requestActivity.launch(intent)
    }

    private fun initQuestionRecyclerView() {

        val onQuestionClickListener = { question: QuestionGetResponseVO ->

            val hopeTime =
                question.hopeTutoringTime?.map { "${it.hour}시 ${it.minute}분" }
                    ?.joinToString(", ")

            startQuestionDetailActivity(question, hopeTime)


        }

        questionAdapter =
            QuestionAdapter(onQuestionClickListener).apply {
                setHasStableIds(true)
            }

        binding.rvQuestion.apply {
            adapter = questionAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun initReviewRecyclerView() {

        reviewAdapter = ReviewAdapter()

        binding.rvReview.apply {
            adapter = reviewAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun initEventAdapter() {
        eventAdapter = EventAdapter { url ->
            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(url)
            }
            startActivity(intent)
        }
        PagerSnapHelper().attachToRecyclerView(binding.rvEvent)

        binding.rvEvent.apply {
            adapter = eventAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        eventAdapter.setItems(
            EventsVO(
                count = 0,
                events = mutableListOf<EventVO>().apply {
                    repeat(5) {
                        add(
                            EventVO(
                                createdAt = "",
                                id = "",
                                url = "https://goodgoodminki.tistory.com/entry",
                                image = "https://fastly.picsum.photos/id/715/200/300.jpg?hmac=jMgGkNrRGTz5pgw27YMTCyozftm33Rw2fPKQU2FypW4"
                            )
                        )
                    }
                }
            )
        )
    }

    private fun observe() {
        observeQuestions()
        observeAnswer()
        observeOfferRemove()
        //observeCheck()
        observeMyProfile()
    }

    private fun observeQuestions() {
        questionsViewModel.questions.observe(viewLifecycleOwner) { questions ->
            questionAdapter.submitList(questions)
            if (isCalledFirstTime) {
                isCalledFirstTime = false
                binding.rvQuestion.scrollToPosition(0)
            }
            binding.tvNumOfQuestions.text =
                if (questions.isNotEmpty()) "${questions.size}명의 학생이 선생님을 기다리고 있어요"
                else "아직 질문이 올라오지 않았어요"
        }
    }

    private fun observeAnswer() {
        answerViewModel.answer.observe(viewLifecycleOwner) {
        }
    }

    private fun observeOfferRemove() {
        offerRemoveViewModel.notiOfferRemove.observe(viewLifecycleOwner) {
            /*if (it != SUCCESS_OFFER_REMOVE) {
                Log.d("error", "failed to remove offer")
            }*/
        }
    }

    private fun observeMyProfile() {
        myProfileViewModel.amount.observe(viewLifecycleOwner) {
            binding.cbCoin.coin = it * 100
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

    companion object {
        const val IMAGE = "image"
        const val SUBJECT = "subject"
        const val DIFFICULTY = "difficulty"
        const val DESCRIPTION = "description"
        const val QUESTION_ID = "questionId"
        const val HOPE_TIME = "hopeTime"
        const val OFFERED_ALREADY = "offeredAlready"
    }
}
