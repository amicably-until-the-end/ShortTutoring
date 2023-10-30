package org.softwaremaestro.presenter.student_home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.question_get.entity.QuestionGetResponseVO
import org.softwaremaestro.domain.socket.SocketManager
import org.softwaremaestro.domain.tutoring_get.entity.TutoringVO
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentStudentHomeBinding
import org.softwaremaestro.presenter.my_page.viewmodel.FollowingViewModel
import org.softwaremaestro.presenter.question_upload.question_normal_upload.QuestionNormalFormFragment
import org.softwaremaestro.presenter.question_upload.question_normal_upload.QuestionUploadActivity
import org.softwaremaestro.presenter.question_upload.question_selected_upload.QuestionReserveActivity
import org.softwaremaestro.presenter.student_home.adapter.EventAdapter
import org.softwaremaestro.presenter.student_home.adapter.LectureAdapter
import org.softwaremaestro.presenter.student_home.adapter.StudentQuestionAdapter
import org.softwaremaestro.presenter.student_home.adapter.TeacherCircularAdapter
import org.softwaremaestro.presenter.student_home.adapter.TeacherSimpleAdapter
import org.softwaremaestro.presenter.student_home.viewmodel.EventViewModel
import org.softwaremaestro.presenter.student_home.viewmodel.MyProfileViewModel
import org.softwaremaestro.presenter.student_home.viewmodel.ReviewViewModel
import org.softwaremaestro.presenter.student_home.viewmodel.TeacherOnlineViewModel
import org.softwaremaestro.presenter.student_home.viewmodel.TutoringViewModel
import org.softwaremaestro.presenter.student_home.widget.TeacherProfileDialog
import org.softwaremaestro.presenter.teacher_home.viewmodel.QuestionViewModel
import org.softwaremaestro.presenter.teacher_profile.viewmodel.BestTeacherViewModel
import org.softwaremaestro.presenter.teacher_profile.viewmodel.FollowUserViewModel
import org.softwaremaestro.presenter.util.Util
import org.softwaremaestro.presenter.util.Util.toLocalDateTime
import org.softwaremaestro.presenter.util.Util.toPx
import org.softwaremaestro.presenter.util.widget.SimpleAlertDialog
import org.softwaremaestro.presenter.video_player.VideoPlayerActivity
import java.time.LocalDateTime

@AndroidEntryPoint
class StudentHomeFragment : Fragment() {

    private lateinit var binding: FragmentStudentHomeBinding

    private val followingViewModel: FollowingViewModel by activityViewModels()
    private val teacherOnlineViewModel: TeacherOnlineViewModel by activityViewModels()
    private val followUserViewModel: FollowUserViewModel by activityViewModels()
    private val myProfileViewModel: MyProfileViewModel by activityViewModels()
    private val bestTeacherViewModel: BestTeacherViewModel by activityViewModels()
    private val tutoringViewModel: TutoringViewModel by activityViewModels()
    private val eventViewModel: EventViewModel by activityViewModels()
    private val reviewsViewModel: ReviewViewModel by activityViewModels()
    private val questionViewModel: QuestionViewModel by viewModels()

    private lateinit var teacherFollowingAdapter: TeacherCircularAdapter
    private lateinit var teacherOnlineAdapter: TeacherCircularAdapter
    private lateinit var questionReservedAdapter: StudentQuestionAdapter
    private lateinit var questionPendingAdapter: StudentQuestionAdapter
    private lateinit var lectureAdapter: LectureAdapter
    private lateinit var teacherAdapter: TeacherSimpleAdapter
    private lateinit var eventAdapter: EventAdapter
    private lateinit var dialogTeacherProfile: TeacherProfileDialog
    private lateinit var followings: List<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentHomeBinding.inflate(layoutInflater)
        getRemoteData()
        initTeacherProfileDialog()
        setCoinButton()
        setQuestionButton()
        setRecyclerViews()
        setMoreTeacherBtn()
        setNofiBtn()
        setObserver()

        return binding.root
    }

    private fun getRemoteData() {
        myProfileViewModel.getMyProfile()
        bestTeacherViewModel.getTeachers()
        tutoringViewModel.getTutoring()
        SocketManager.userId?.let { followingViewModel.getFollowing(it) }
        teacherOnlineViewModel.getTeacherOnlines()
        eventViewModel.getEvents()
        questionViewModel.getMyQuestions()
    }

    private fun initTeacherProfileDialog() {
        dialogTeacherProfile = TeacherProfileDialog(
            onProfileClick = { teacherId ->
//                startActivityForResult(
//                    Intent(
//                        requireActivity(),
//                        TeacherProfileActivity::class.java
//                    ).apply {
//                        putExtra("teacher-id", teacherId)
//                    }, QUESTION_UPLOAD_RESULT
//                )
//
//                dialogTeacherProfile.dismiss()
            },
            onUnfollow = { teacherId ->
                followUserViewModel.unfollowUser(teacherId)
                Util.createToast(requireActivity(), "선생님 찜하기가 해제되었습니다").show()
                // teacher의 followers를 갱신하기 위해 getTeachers() 호출
                bestTeacherViewModel.getTeachers()
            },
            onFollow = { teacherId ->
                followUserViewModel.followUser(teacherId)
                Util.createToast(requireActivity(), "선생님을 찜했습니다").show()
                // teacher의 followers를 갱신하기 위해 getTeachers() 호출
                bestTeacherViewModel.getTeachers()
            },
            onReserve = { teacherId ->
                startActivityForResult(
                    Intent(
                        requireActivity(),
                        QuestionReserveActivity::class.java
                    ).apply {
                        putExtra("teacher-id", teacherId)
                    }, QUESTION_UPLOAD_RESULT
                )

                dialogTeacherProfile.dismiss()
            },
            onDismiss = {
                // 선생님 뷰의 followers를 갱신하기 위해 ViewModel의 메서드 호출
                teacherOnlineViewModel.getTeacherOnlines()
                SocketManager.userId?.let {
                    followingViewModel.getFollowing(it)
                }
            }
        )
    }

    private fun setOthersQuestionRecyclerView() {
        return
    }

    private fun setRecyclerViews() {
        setTeacherFollowingRecyclerView()
        setTeacherOnlineRecyclerView()
        setOthersQuestionRecyclerView()
        setQuestionReservedRecyclerView()
        setQuestionPendingRecyclerView()
        setLectureRecyclerView()
        setTeacherRecyclerView()
        setEventRecyclerView()
    }

    private fun setTeacherFollowingRecyclerView() {
        teacherFollowingAdapter = TeacherCircularAdapter {
            dialogTeacherProfile.setItem(it)
            it.teacherId?.let {
                reviewsViewModel.getReviews(it)
            }

            dialogTeacherProfile.show(parentFragmentManager, "teacherProfile")
        }

        binding.rvTeacherFollowing.apply {
            adapter = teacherFollowingAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setTeacherOnlineRecyclerView() {
        teacherOnlineAdapter = TeacherCircularAdapter {
            dialogTeacherProfile.setItem(it)
            it.teacherId?.let {
                reviewsViewModel.getReviews(it)
                // Todo: 선생님 tutoring 가져올 수 있게 되면 변경
                // tutoringViewModel.getTutoring()
            }
            dialogTeacherProfile.show(parentFragmentManager, "teacherProfile")
        }

        binding.rvTeacherOnline.apply {
            adapter = teacherOnlineAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }


    private fun setTeacherRecyclerView() {

        teacherAdapter = TeacherSimpleAdapter { teacherVO ->
            dialogTeacherProfile.setItem(teacherVO)
            teacherVO.teacherId?.let {
                reviewsViewModel.getReviews(it)
                // Todo: 선생님 tutoring 가져올 수 있게 되면 변경
                // tutoringViewModel.getTutoring()
            }
            dialogTeacherProfile.show(parentFragmentManager, "teacherProfile")
        }

        binding.rvTeacher.apply {
            adapter = teacherAdapter
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
            smoothScrollToPosition(0)
        }
        setAutoScrollToEventRecycler()
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
                toPx(NORMAL_EVENT_BUTTON_SIZE, requireContext()),
                toPx(NORMAL_EVENT_BUTTON_SIZE, requireContext())
            ).apply {
                marginStart = toPx(EVENT_BUTTON_SIZE_MARGIN, requireContext())
                marginEnd = toPx(EVENT_BUTTON_SIZE_MARGIN, requireContext())
            }
        }
    }

    private fun setFocusedToEventButtonAt(pos: Int) {
        binding.containerEventBtn.getChildAt(pos)?.let {
            it.layoutParams = LinearLayout.LayoutParams(
                toPx(FOCUSED_EVENT_BUTTON_SIZE, requireContext()),
                toPx(FOCUSED_EVENT_BUTTON_SIZE, requireContext())
            ).apply {
                marginStart = toPx(2, requireContext())
                marginEnd = toPx(2, requireContext())
            }
        }
    }

    private fun setAutoScrollToEventRecycler() {
        var pos = 0
        viewLifecycleOwner.lifecycleScope.launch {
            while (NonCancellable.isActive) {
                binding.rvEvent.smoothScrollToPosition(pos)
                delay(10000L)
                pos = (pos + 1) % eventAdapter.itemCount
            }
        }
    }

    /**
     * 이벤트 배너가 중앙에 오도록 좌우 패딩을 조정
     */
    private fun setHorizontalPaddingTo(rv: RecyclerView, viewWidthDP: Int) {
        val displayWidth = resources.displayMetrics.widthPixels
        val viewWidthPx = toPx(viewWidthDP, requireContext())
        val padding = (displayWidth - viewWidthPx) / 2
        rv.setPadding(padding, 0, padding, 0)
    }

    private fun setMoreTeacherBtn() {
//        binding.containerMoreTeacher.setOnClickListener {
//            startActivity(Intent(requireActivity(), TeacherSearchActivity::class.java))
//        }
    }

    private fun setNofiBtn() {
        binding.btnToolbarNotification.setOnClickListener {
//            showNoti("제목", "본문") {
//                // when confirm clicked
//            }
        }
    }

    private fun setQuestionReservedRecyclerView() {
        questionReservedAdapter = StudentQuestionAdapter {
            (requireActivity() as StudentHomeActivity).moveToChatTab(it.chattingId ?: it.id)
        }

        binding.rvMyReservedQuestion.apply {
            adapter = questionReservedAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setQuestionPendingRecyclerView() {
        questionPendingAdapter = StudentQuestionAdapter {
            (requireActivity() as StudentHomeActivity).moveToChatTab(it.chattingId ?: it.id)
        }

        binding.rvMyPendingQuestion.apply {
            adapter = questionPendingAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setLectureRecyclerView() {

        lectureAdapter = LectureAdapter {
            watchRecordFile(it)
        }

        binding.rvLecture.apply {
            adapter = lectureAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun watchRecordFile(tutoringVO: TutoringVO) {
        val tutoringUrl = tutoringVO.recordFileUrl?.get(0) ?: run {
            SimpleAlertDialog().apply {
                title = "아직 영상이 생성되는 중입니다"
                description = "잠시 후 다시 시도해주세요"
            }.show(parentFragmentManager, "making video")
            return
        }

        val intent = Intent(requireActivity(), VideoPlayerActivity::class.java).apply {
            putExtra(PROFILE_IMAGE, tutoringVO.opponentProfileImage)
            putExtra(STUDENT_NAME, tutoringVO.opponentName)
            putExtra(SCHOOL_LEVEL, tutoringVO.schoolLevel)
            putExtra(SUBJECT, tutoringVO.schoolSubject)
            putExtra(DESCRIPTION, tutoringVO.description)
            putExtra(RECORDING_FILE_URL, tutoringUrl)
            val teacherId = tutoringVO.opponentId
            val following = teacherId in followings
            putExtra(TEACHER_ID, teacherId)
            putExtra(FOLLOWING, following)
        }
        startActivity(intent)
    }

    private fun setCoinButton() {
        binding.cbCoin.setOnClickListener {
//            startActivity(Intent(requireContext(), ChargeCoinActivity::class.java).apply {
//                putExtra("my-coin", myProfileViewModel.amount.value)
//            })
            (requireActivity() as StudentHomeActivity).moveToCoinTab()
        }
    }

    private fun setQuestionButton() {
        binding.btnQuestion.setOnClickListener {
            startQuestionUploadActivity()

            disableQuestionBtnFor(500L)
        }
    }

    private fun startQuestionUploadActivity() {
        val intent = Intent(requireContext(), QuestionUploadActivity::class.java)
        startActivityForResult(intent, QUESTION_UPLOAD_RESULT)
    }

    private fun disableQuestionBtnFor(l: Long) {
        binding.btnQuestion.isEnabled = false
        binding.btnQuestion.setBackgroundColor(
            resources.getColor(R.color.background_light_blue, null)
        )
        binding.tvQuestion.setTextColor(resources.getColor(R.color.primary_blue, null))
        binding.ivCamera.backgroundTintList =
            resources.getColorStateList(R.color.primary_blue, null)
        binding.ivArrowRight.backgroundTintList =
            resources.getColorStateList(R.color.primary_blue, null)

        Handler(Looper.getMainLooper()).postDelayed({
            binding.btnQuestion.isEnabled = true
            binding.btnQuestion.setBackgroundResource(R.drawable.bg_radius_5_grad_blue)
            binding.tvQuestion.setTextColor(resources.getColor(R.color.white, null))
            binding.ivCamera.backgroundTintList = resources.getColorStateList(R.color.white, null)
            binding.ivArrowRight.backgroundTintList =
                resources.getColorStateList(R.color.white, null)
        }, l)
    }

    private fun observeFollowing() {
        followingViewModel.following.observe(viewLifecycleOwner) {
            it ?: return@observe

            followings = it.map { it.teacherId }.filterNotNull()
            if (it.isNotEmpty()) {
                binding.containerMyTeacherSection.visibility = View.VISIBLE
                binding.nsTeacherFollowing.visibility = View.VISIBLE
                if (!teacherOnlineViewModel.teacherOnlines.value.isNullOrEmpty()) {
                    binding.dvTeacher.visibility = View.VISIBLE
                }
            } else {
                binding.dvTeacher.visibility = View.GONE
                binding.nsTeacherFollowing.visibility = View.GONE
                if (teacherOnlineViewModel.teacherOnlines.value.isNullOrEmpty()) {
                    binding.containerMyTeacherSection.visibility = View.GONE
                } else {
                    binding.containerMyTeacherSection.visibility = View.VISIBLE
                }
            }
            teacherFollowingAdapter.setItem(
                if (teacherOnlineViewModel.teacherOnlines.value.isNullOrEmpty()) it
                else it.take(3)
            )
            teacherFollowingAdapter.notifyDataSetChanged()
        }
    }

    private fun observeAmount() {
        myProfileViewModel.amount.observe(viewLifecycleOwner) {
            binding.cbCoin.coin = it * 100
        }
    }

    private fun observeMyQuestions() {
        questionViewModel.myQuestions.observe(viewLifecycleOwner) { questions ->
            questions ?: return@observe
            val fastestReserved = getFastestReserved(questions)
            binding.containerMyReservedQuestion.visibility =
                if (fastestReserved.isNotEmpty()) View.VISIBLE else View.GONE

            questionReservedAdapter.submitList(fastestReserved)
            questionPendingAdapter.notifyDataSetChanged()

            val pendings = questions.filter { it.status == "pending" }
            binding.containerMyPendingQuestion.visibility =
                if (pendings.isEmpty()) View.GONE else View.VISIBLE
            binding.tvNumMyPendingQuestion.text = "${pendings.size}"

            questionPendingAdapter.submitList(pendings)
            questionPendingAdapter.notifyDataSetChanged()

            binding.dvMyQuestion.visibility =
                if (fastestReserved.isEmpty() || pendings.isEmpty()) View.GONE
                else View.VISIBLE

            binding.containerMyQuestionSection.visibility =
                if (fastestReserved.isEmpty() && pendings.isEmpty()) View.GONE
                else View.VISIBLE
        }
    }

    private fun getFastestReserved(questions: List<QuestionGetResponseVO>): List<QuestionGetResponseVO> {
        val fastestReserved = questions.filter { it.status == "reserved" }
            .sortedWith { q1, q2 ->
                val d1 = q1.reservedStart ?: return@sortedWith 1
                val d2 = q2.reservedStart ?: return@sortedWith -1
                return@sortedWith if (toLocalDateTime(d1) < toLocalDateTime(d2)) 1 else -1
            }.filter {
                it.reservedStart ?: return@filter false
                val ldt = toLocalDateTime(it.reservedStart!!)
                val now = LocalDateTime.now()
                ldt >= now && ldt < now.plusDays(1L)
            }.take(1)
        return fastestReserved
    }

    private fun observeTutoring() {
        tutoringViewModel.tutoring.observe(viewLifecycleOwner) {
            binding.containerLectureSection.visibility =
                if (it.isEmpty()) View.GONE else View.VISIBLE
            // 시간순 배열
            val sTutorings = it.sortedWith { t1, t2 ->
                val d1 = t1.tutoringDate ?: return@sortedWith 1
                val d2 = t2.tutoringDate ?: return@sortedWith -1
                return@sortedWith if (d1 < d2) 1 else -1
            }
            lectureAdapter.setItem(sTutorings)
            lectureAdapter.notifyDataSetChanged()
        }
    }

    private fun observeTeachers() {
        bestTeacherViewModel.bestTeachers.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.dvRanking.visibility = View.VISIBLE
                binding.containerBestTeacherSection.visibility = View.VISIBLE
            } else {
                binding.dvRanking.visibility = View.GONE
                binding.containerBestTeacherSection.visibility = View.GONE
            }
            teacherAdapter.setItem(it)
            teacherAdapter.notifyDataSetChanged()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                QUESTION_UPLOAD_RESULT -> {
                    val questionId =
                        data?.getStringExtra(QuestionNormalFormFragment.QUESTION_UPLOAD_RESULT)
                    Log.d("StudentHome Link", questionId.toString())
                    (activity as StudentHomeActivity).apply {
                        moveToChatTab(questionId)
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

    private fun setObserver() {
        observeFollowing()
        observeAmount()
        observeMyQuestions()
        observeTutoring()
        observeReview()
        observeTeachers()
        observeTeacherOnlines()
        observeEvents()
    }

    private fun observeTeacherOnlines() {
        teacherOnlineViewModel.teacherOnlines.observe(viewLifecycleOwner) {

            teacherOnlineAdapter.setItem(it)
            teacherOnlineAdapter.notifyDataSetChanged()

            if (it.isNotEmpty()) {
                binding.containerMyTeacherSection.visibility = View.VISIBLE
                binding.nsTeacherOnline.visibility = View.VISIBLE
                if (!followingViewModel.following.value.isNullOrEmpty()) {
                    binding.dvTeacher.visibility = View.VISIBLE
                }
            } else {
                binding.dvTeacher.visibility = View.GONE
                binding.nsTeacherOnline.visibility = View.GONE
                if (followingViewModel.following.value.isNullOrEmpty()) {
                    binding.containerMyTeacherSection.visibility = View.GONE
                } else {
                    binding.containerMyTeacherSection.visibility = View.VISIBLE
                }
            }
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
        reviewsViewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            reviews ?: return@observe
            val reviewsNotEmpty =
                reviews.filter { it.reviewComment != null && it.reviewComment!!.length >= 3 }
            dialogTeacherProfile.setItemToReviewRecyclerView(reviewsNotEmpty)
            reviewsViewModel.setReviews(null)
        }
    }

    private fun initEventButton(numEvent: Int) {
        if (binding.containerEventBtn.isNotEmpty()) return
        repeat(numEvent) {
            binding.containerEventBtn.addView(
                AppCompatButton(requireContext()).apply {
                    val size = if (it == 0) {
                        toPx(FOCUSED_EVENT_BUTTON_SIZE, requireContext())
                    } else {
                        toPx(NORMAL_EVENT_BUTTON_SIZE, requireContext())
                    }
                    layoutParams = LinearLayout.LayoutParams(size, size).apply {
                        marginStart = toPx(EVENT_BUTTON_SIZE_MARGIN, requireContext())
                        marginEnd = toPx(EVENT_BUTTON_SIZE_MARGIN, requireContext())
                    }
                    setBackgroundResource(R.drawable.bg_radius_100_primary_blue)
                    stateListAnimator = null
                }
            )
        }
    }

    companion object {
        const val QUESTION_UPLOAD_RESULT = 1001
        const val CLASSROOM_END_RESULT = 1002
        private const val EVENT_ITEM_WIDTH = 360
        private const val FOCUSED_EVENT_BUTTON_SIZE = 12
        private const val NORMAL_EVENT_BUTTON_SIZE = 9
        private const val EVENT_BUTTON_SIZE_MARGIN = 6

        const val PROFILE_IMAGE = "profile-image"
        const val STUDENT_NAME = "student-name"
        const val SCHOOL_LEVEL = "school-level"
        const val SUBJECT = "subject"
        const val DESCRIPTION = "description"
        const val RECORDING_FILE_URL = "recording-file-url"
        const val TEACHER_ID = "teacher-id"
        const val FOLLOWING = "following"
    }
}