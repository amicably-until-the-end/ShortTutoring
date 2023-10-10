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
import org.softwaremaestro.domain.question_get.entity.QuestionGetResponseVO
import org.softwaremaestro.domain.socket.SocketManager
import org.softwaremaestro.presenter.databinding.FragmentTeacherHomeBinding
import org.softwaremaestro.presenter.student_home.viewmodel.MyProfileViewModel
import org.softwaremaestro.presenter.teacher_home.adapter.QuestionAdapter
import org.softwaremaestro.presenter.teacher_home.adapter.ReviewAdapter
import org.softwaremaestro.presenter.teacher_home.viewmodel.OfferRemoveViewModel
import org.softwaremaestro.presenter.teacher_home.viewmodel.QuestionsViewModel
import org.softwaremaestro.presenter.util.widget.SimpleAlertDialog

private const val REFRESHING_TIME_INTERVAL = 10000L

@AndroidEntryPoint
class TeacherHomeFragment : Fragment() {

    private lateinit var binding: FragmentTeacherHomeBinding
    private val questionsViewModel: QuestionsViewModel by viewModels()
    private val myProfileViewModel: MyProfileViewModel by viewModels()
    private val offerRemoveViewModel: OfferRemoveViewModel by viewModels()

    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var waitingSnackbar: Snackbar
    private var isCalledFirstTime = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTeacherHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myProfileViewModel.getMyProfile()

        setTexts()

        initWaitingSnackbar()
        initQuestionRecyclerView()
        initReviewRecyclerView()

        keepGettingQuestions(REFRESHING_TIME_INTERVAL)

        observe()
    }

    private fun setTexts() {


//        binding.tvRatingAndTemperature.text =
//            "현재 별점은 %.1f점, 매너 온도는 %d도에요".format(TEACHER_RATING, TEACHER_TEMPERATURE)
//
//        binding.btnAnswerCost.text = DecimalFormat("###,###").format(TEACHER_ANSWER_COST) + "원"


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

        val onQuestionClickListener = { question: QuestionGetResponseVO ->

            val hopeTime =
                question.hopeTutoringTime?.map { "${it.hour}시 ${it.minute}분" }
                    ?.joinToString(", ")
            Log.d("my question", "$question $hopeTime")


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
            startActivityForResult(intent, QUESTION_DETAIL_ACTIVITY)
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

    private fun observe() {
        observeQuestions()
        observeOfferRemove()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            QUESTION_DETAIL_ACTIVITY -> {
                when (resultCode) {
                    OFFER_SUCCESS -> {
                        SimpleAlertDialog().apply {
                            title = "수업 요청에 성공했습니다"
                            description = "채팅 페이지에서 수업 내용을 협의할 수 있어요"
                        }.show(parentFragmentManager, "simpleAlertDialog")
                    }
                }
            }
        }
    }

    companion object {
        const val QUESTION_DETAIL_ACTIVITY = 0
        const val OFFER_SUCCESS = 1
        const val OFFER_FAIL = 2

        const val IMAGE = "image"
        const val SUBJECT = "subject"
        const val DESCRIPTION = "description"
        const val QUESTION_ID = "questionId"
        const val HOPE_TIME = "hopeTime"
        const val OFFERED_ALREADY = "offeredAlready"
    }
}
