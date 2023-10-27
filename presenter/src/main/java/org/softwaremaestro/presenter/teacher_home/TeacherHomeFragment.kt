package org.softwaremaestro.presenter.teacher_home

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.children
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.question_get.entity.QuestionGetResponseVO
import org.softwaremaestro.domain.socket.SocketManager
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentTeacherHomeBinding
import org.softwaremaestro.presenter.student_home.adapter.EventAdapter
import org.softwaremaestro.presenter.student_home.viewmodel.EventViewModel
import org.softwaremaestro.presenter.student_home.viewmodel.HomeViewModel
import org.softwaremaestro.presenter.student_home.viewmodel.MyProfileViewModel
import org.softwaremaestro.presenter.student_home.viewmodel.ReviewViewModel
import org.softwaremaestro.presenter.teacher_home.QuestionDetailActivity.Companion.CHAT_ID
import org.softwaremaestro.presenter.teacher_home.QuestionDetailActivity.Companion.OFFER_RESULT
import org.softwaremaestro.presenter.teacher_home.QuestionDetailActivity.Companion.OFFER_SUCCESS
import org.softwaremaestro.presenter.teacher_home.adapter.ReviewAdapter
import org.softwaremaestro.presenter.teacher_home.adapter.TeacherQuestionAdapter
import org.softwaremaestro.presenter.teacher_home.viewmodel.AnswerViewModel
import org.softwaremaestro.presenter.teacher_home.viewmodel.CheckViewModel
import org.softwaremaestro.presenter.teacher_home.viewmodel.OfferRemoveViewModel
import org.softwaremaestro.presenter.teacher_home.viewmodel.QuestionViewModel
import org.softwaremaestro.presenter.util.Util
import org.softwaremaestro.presenter.util.Util.logError
import java.time.LocalDateTime

private const val REFRESHING_TIME_INTERVAL = 10000L

@AndroidEntryPoint
class TeacherHomeFragment : Fragment() {

    private lateinit var binding: FragmentTeacherHomeBinding
    private val questionViewModel: QuestionViewModel by viewModels()
    private val myProfileViewModel: MyProfileViewModel by viewModels()
    private val answerViewModel: AnswerViewModel by viewModels()
    private val offerRemoveViewModel: OfferRemoveViewModel by viewModels()
    private val checkViewModel: CheckViewModel by viewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val eventViewModel: EventViewModel by activityViewModels()
    private val reviewsViewModel: ReviewViewModel by activityViewModels()
    private lateinit var requestActivity: ActivityResultLauncher<Intent>

    private lateinit var questionAdapter: TeacherQuestionAdapter
    private lateinit var questionReservedAdapter: TeacherQuestionAdapter
    private lateinit var questionPendingAdapter: TeacherQuestionAdapter
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

        getRemoteData()
        setTexts()
        initWaitingSnackbar()
        initQuestionRecyclerView()
        initReviewRecyclerView()
        setEventRecyclerView()
        keepGettingQuestions(REFRESHING_TIME_INTERVAL)
        setRefreshContainer()
        observe()
    }

    private fun getRemoteData() {
        myProfileViewModel.getMyProfile()
        eventViewModel.getEvents()
        SocketManager.userId?.let { reviewsViewModel.getReviews(it) }
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

        setQuestionRecyclerView()
        setReservedQuestionRecyclerView()
        setPendingQuestionRecyclerView()
    }

    private fun setQuestionRecyclerView() {
        val onQuestionClickListener = { question: QuestionGetResponseVO ->

            val hopeTime =
                question.hopeTutoringTime?.map { "${it.hour}시 ${it.minute}분" }
                    ?.joinToString(", ")

            startQuestionDetailActivity(question, hopeTime)
        }

        questionAdapter =
            TeacherQuestionAdapter(onQuestionClickListener).apply {
                setHasStableIds(true)
            }

        binding.rvMyQuestion.apply {
            adapter = questionAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setReservedQuestionRecyclerView() {
        questionReservedAdapter =
            TeacherQuestionAdapter {
                (requireActivity() as TeacherHomeActivity).moveToChatTab(it.chattingId)
            }

        binding.rvMyReservedQuestion.apply {
            adapter = questionReservedAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setPendingQuestionRecyclerView() {
        questionPendingAdapter =
            TeacherQuestionAdapter {
                (requireActivity() as TeacherHomeActivity).moveToChatTab(it.chattingId)
            }

        binding.rvMyPendingQuestion.apply {
            adapter = questionPendingAdapter
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

    private fun setEventRecyclerView() {
        eventAdapter = EventAdapter { url ->
            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                addCategory(Intent.CATEGORY_BROWSABLE)
                data = Uri.parse(url)
            }
            startActivity(intent)
        }

        PagerSnapHelper().attachToRecyclerView(binding.rvEvent)

        binding.rvEvent.apply {
            adapter = eventAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setEventButton()
        }

        setHorizontalPaddingTo(binding.rvEvent, EVENT_ITEM_WIDTH)
    }

    private fun setEventButton() {
        binding.rvEvent.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    resetEventButton()
                    val pos =
                        (binding.rvEvent.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    setFocusedToEventButtonAt(pos)
                }
            }
        })
    }

    private fun resetEventButton() {
        binding.containerEventBtn.children.forEach { child ->
            child.layoutParams = LinearLayout.LayoutParams(
                Util.toPx(NORMAL_EVENT_BUTTON_SIZE, requireContext()),
                Util.toPx(NORMAL_EVENT_BUTTON_SIZE, requireContext())
            ).apply {
                marginStart =
                    Util.toPx(EVENT_BUTTON_SIZE_MARGIN, requireContext())
                marginEnd =
                    Util.toPx(EVENT_BUTTON_SIZE_MARGIN, requireContext())
            }
        }
    }

    private fun setFocusedToEventButtonAt(pos: Int) {
        binding.containerEventBtn.getChildAt(pos)?.let {
            it.layoutParams = LinearLayout.LayoutParams(
                Util.toPx(FOCUSED_EVENT_BUTTON_SIZE, requireContext()),
                Util.toPx(FOCUSED_EVENT_BUTTON_SIZE, requireContext())
            ).apply {
                marginStart = Util.toPx(2, requireContext())
                marginEnd = Util.toPx(2, requireContext())
            }
        }
    }

    /**
     * 이벤트 배너가 중앙에 오도록 좌우 패딩을 조정
     */
    private fun setHorizontalPaddingTo(rv: RecyclerView, viewWidthDP: Int) {
        val displayWidth = resources.displayMetrics.widthPixels
        val viewWidthPx = Util.toPx(viewWidthDP, requireContext())
        val padding = (displayWidth - viewWidthPx) / 2
        rv.setPadding(padding, 0, padding, 0)
    }

    private fun setRefreshContainer() {
        binding.containerMyQuestionEmpty.setOnClickListener {
            binding.tvRefresh1.setTextColor(resources.getColor(R.color.sub_text_grey, null))
            binding.tvRefresh2.setTextColor(resources.getColor(R.color.sub_text_grey, null))
            binding.tvRefresh3.setTextColor(resources.getColor(R.color.sub_text_grey, null))
            binding.ivRefresh.backgroundTintList = resources.getColorStateList(
                R.color.sub_text_grey, null
            )

            Handler(Looper.getMainLooper()).postDelayed({
                binding.tvRefresh1.setTextColor(resources.getColor(R.color.black, null))
                binding.tvRefresh2.setTextColor(resources.getColor(R.color.black, null))
                binding.tvRefresh3.setTextColor(resources.getColor(R.color.primary_blue, null))
                binding.ivRefresh.backgroundTintList =
                    resources.getColorStateList(R.color.primary_blue, null)
            }, 500L)

            questionViewModel.getQuestions()
        }
    }

    private fun observe() {
        observeQuestions()
        observeMyQuestions()
        observeOfferRemove()
        //observeCheck()
        observeMyProfile()
        observeEvents()
        observeReview()
    }

    private fun observeQuestions() {
        questionViewModel.questions.observe(viewLifecycleOwner) { questions ->
            questions ?: run {
                logError(this@TeacherHomeFragment::class.java, "myQuestions is null")
                return@observe
            }
            val questionsNotOffered = getQuestionsNotOffered(questions)

            questionAdapter.submitList(questionsNotOffered)
            questionAdapter.notifyDataSetChanged()

            if (isCalledFirstTime) {
                isCalledFirstTime = false
                binding.rvMyQuestion.scrollToPosition(0)
            }
            if (questionsNotOffered.isNotEmpty()) {
                binding.tvNumOfQuestions.text = "${questionsNotOffered.size}명의 학생이 선생님을 기다리고 있어요"
                binding.containerMyQuestionEmpty.visibility = View.GONE
                binding.rvMyQuestion.visibility = View.VISIBLE
            } else {
                binding.tvNumOfQuestions.text = "아직 질문이 올라오지 않았어요"
                binding.containerMyQuestionEmpty.visibility = View.VISIBLE
                binding.rvMyQuestion.visibility = View.GONE
            }
        }
    }

    private fun observeMyQuestions() {
        questionViewModel.questions.observe(viewLifecycleOwner) { questions ->
            questions ?: run {
                logError(this@TeacherHomeFragment::class.java, "questions is null")
                return@observe
            }
            val pendings = getPendings(questions)
            binding.containerMyPendingQuestion.visibility =
                if (pendings.isNotEmpty()) View.VISIBLE else View.GONE
            binding.tvNumMyPendingQuestion.text = "${pendings.size}"
            questionPendingAdapter.submitList(pendings)
            questionPendingAdapter.notifyDataSetChanged()

            val fastestReserved = getFastestReserved(questions)
            binding.containerMyReservedQuestion.visibility =
                if (fastestReserved.isNotEmpty()) View.VISIBLE else View.GONE

            questionReservedAdapter.submitList(fastestReserved)
            questionPendingAdapter.notifyDataSetChanged()

            binding.containerMyQuestionSection.visibility =
                if (pendings.isEmpty() && fastestReserved.isEmpty()) View.GONE else View.VISIBLE


            binding.dvMyQuestion.visibility =
                if (pendings.isEmpty() || fastestReserved.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun getPendings(questions: List<QuestionGetResponseVO>): List<QuestionGetResponseVO> {
        val userId = SocketManager.userId ?: run {
            Toast.makeText(requireContext(), "사용자의 ID를 가져오는데 실패했습니다", Toast.LENGTH_SHORT).show()
            return emptyList()
        }

        val pendings = questions.filter {
            it.status == "pending"
        }.filter {
            it.offerTeachers ?: return@filter false
            userId in it.offerTeachers!!
        }.sortedWith { q1, q2 ->
            val d1 = q1.reservedStart ?: return@sortedWith 1
            val d2 = q2.reservedStart ?: return@sortedWith -1
            return@sortedWith if (Util.toLocalDateTime(d1) < Util.toLocalDateTime(d2)) 1 else -1
        }
        return pendings
    }

    private fun getFastestReserved(questions: List<QuestionGetResponseVO>): List<QuestionGetResponseVO> {
        val fastestReserved = questions.filter { it.status == "reserved" }
            .sortedWith { q1, q2 ->
                val d1 = q1.reservedStart ?: return@sortedWith 1
                val d2 = q2.reservedStart ?: return@sortedWith -1
                return@sortedWith if (Util.toLocalDateTime(d1) < Util.toLocalDateTime(d2)) 1 else -1
            }.filter {
                it.reservedStart ?: return@filter false
                val ldt = Util.toLocalDateTime(it.reservedStart!!)
                ldt < LocalDateTime.now().plusDays(1L)
            }.take(1)
        return fastestReserved
    }

    private fun getQuestionsNotOffered(questions: List<QuestionGetResponseVO>): List<QuestionGetResponseVO> {
        val userId = SocketManager.userId ?: run {
            Toast.makeText(requireContext(), "사용자의 ID를 가져오는데 실패했습니다", Toast.LENGTH_SHORT).show()
            return emptyList()
        }

        val questionsNotOffered = questions.filter {
            it.status == "pending"
        }.filter {
            it.offerTeachers ?: return@filter false
            userId !in it.offerTeachers!!
        }.sortedWith { q1, q2 ->
            val d1 = q1.reservedStart ?: return@sortedWith 1
            val d2 = q2.reservedStart ?: return@sortedWith -1
            return@sortedWith if (Util.toLocalDateTime(d1) < Util.toLocalDateTime(d2)) 1 else -1
        }

        return questionsNotOffered
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

    private fun observeEvents() {
        eventViewModel.events.observe(viewLifecycleOwner) {
            it?.let {
                eventAdapter.setItems(it)
                eventAdapter.notifyDataSetChanged()
            }
            it.events?.let {
                initEventButton(it.size)
            }
        }
    }

    private fun observeReview() {
        reviewsViewModel.reviews.observe(viewLifecycleOwner) {
            it ?: return@observe
            val reviews = it.filter {
                val comments = it.reviewComment
                comments != null && comments.length >= 3
            }
            if (reviews.isEmpty()) {
                binding.containerReview.visibility = View.GONE
            }
            reviewAdapter.setItem(reviews)
            reviewAdapter.notifyDataSetChanged()
        }
    }

    private fun initEventButton(numEvent: Int) {
        if (binding.containerEventBtn.isNotEmpty()) return
        repeat(numEvent) {
            binding.containerEventBtn.addView(
                AppCompatButton(requireContext()).apply {
                    val size = if (it == 0) {
                        Util.toPx(FOCUSED_EVENT_BUTTON_SIZE, requireContext())
                    } else {
                        Util.toPx(NORMAL_EVENT_BUTTON_SIZE, requireContext())
                    }
                    layoutParams = LinearLayout.LayoutParams(size, size).apply {
                        marginStart = Util.toPx(
                            EVENT_BUTTON_SIZE_MARGIN,
                            requireContext()
                        )
                        marginEnd = Util.toPx(
                            EVENT_BUTTON_SIZE_MARGIN,
                            requireContext()
                        )
                    }
                    setBackgroundResource(R.drawable.bg_radius_100_primary_blue)
                    stateListAnimator = null
                }
            )
        }
    }

    private fun keepGettingQuestions(timeInterval: Long) {
        viewLifecycleOwner.lifecycleScope.launch {
            while (NonCancellable.isActive) {
                questionViewModel.getQuestions()
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


        private const val EVENT_ITEM_WIDTH = 360
        private const val FOCUSED_EVENT_BUTTON_SIZE = 12
        private const val NORMAL_EVENT_BUTTON_SIZE = 9
        private const val EVENT_BUTTON_SIZE_MARGIN = 6
    }
}
