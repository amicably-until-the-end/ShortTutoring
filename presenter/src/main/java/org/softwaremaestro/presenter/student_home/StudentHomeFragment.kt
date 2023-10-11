package org.softwaremaestro.presenter.student_home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.socket.SocketManager
import org.softwaremaestro.presenter.databinding.FragmentStudentHomeBinding
import org.softwaremaestro.presenter.my_page.viewmodel.FollowingViewModel
import org.softwaremaestro.presenter.question_upload.question_normal_upload.QuestionNormalFormFragment
import org.softwaremaestro.presenter.question_upload.question_normal_upload.QuestionUploadActivity
import org.softwaremaestro.presenter.question_upload.question_selected_upload.QuestionReserveActivity
import org.softwaremaestro.presenter.student_home.adapter.EventAdapter
import org.softwaremaestro.presenter.student_home.adapter.LectureAdapter
import org.softwaremaestro.presenter.student_home.adapter.TeacherCircularAdapter
import org.softwaremaestro.presenter.student_home.adapter.TeacherSimpleAdapter
import org.softwaremaestro.presenter.student_home.viewmodel.EventViewModel
import org.softwaremaestro.presenter.student_home.viewmodel.LectureViewModel
import org.softwaremaestro.presenter.student_home.viewmodel.MyProfileViewModel
import org.softwaremaestro.presenter.student_home.viewmodel.TeacherOnlineViewModel
import org.softwaremaestro.presenter.student_home.widget.TeacherProfileDialog
import org.softwaremaestro.presenter.teacher_profile.viewmodel.FollowUserViewModel
import org.softwaremaestro.presenter.teacher_profile.viewmodel.TeacherRecommendViewModel
import org.softwaremaestro.presenter.util.widget.SimpleAlertDialog

@AndroidEntryPoint
class StudentHomeFragment : Fragment() {

    private lateinit var binding: FragmentStudentHomeBinding

    private val followingViewModel: FollowingViewModel by activityViewModels()
    private val teacherOnlineViewModel: TeacherOnlineViewModel by activityViewModels()
    private val followUserViewModel: FollowUserViewModel by activityViewModels()
    private val myProfileViewModel: MyProfileViewModel by activityViewModels()
    private val teacherRecommendViewModel: TeacherRecommendViewModel by activityViewModels()
    private val lectureViewModel: LectureViewModel by activityViewModels()
    private val eventViewModel: EventViewModel by activityViewModels()

    private lateinit var teacherFollowingAdapter: TeacherCircularAdapter
    private lateinit var teacherOnlineAdapter: TeacherCircularAdapter
    private lateinit var lectureAdapter: LectureAdapter
    private lateinit var teacherAdapter: TeacherSimpleAdapter
    private lateinit var eventAdapter: EventAdapter
    private lateinit var dialogTeacherProfile: TeacherProfileDialog

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
        teacherRecommendViewModel.getTeachers()
        lectureViewModel.getLectures()
        SocketManager.userId?.let { followingViewModel.getFollowing(it) }
        teacherOnlineViewModel.getTeacherOnlines()
        eventViewModel.getEvents()
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
                Toast.makeText(requireContext(), "선생님을 찜하기가 해제되었습니다", Toast.LENGTH_SHORT).show()
                // teacher의 followers를 갱신하기 위해 getTeachers() 호출
                teacherRecommendViewModel.getTeachers()
            },
            onFollow = { teacherId ->
                followUserViewModel.followUser(teacherId)
                Toast.makeText(requireContext(), "선생님을 찜했습니다", Toast.LENGTH_SHORT).show()
                // teacher의 followers를 갱신하기 위해 getTeachers() 호출
                teacherRecommendViewModel.getTeachers()
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
        setLectureRecyclerView()
        setTeacherRecyclerView()
        setEventAdapter()
    }

    private fun setTeacherFollowingRecyclerView() {
        teacherFollowingAdapter = TeacherCircularAdapter {
            dialogTeacherProfile.setItem(it)
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
            dialogTeacherProfile.show(parentFragmentManager, "teacherProfile")
        }

//        binding.rvTeacher.apply {
//            adapter = teacherAdapter
//            layoutManager =
//                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
//        }
    }

    private fun setEventAdapter() {
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
        }
    }

    private fun setMoreTeacherBtn() {
//        binding.containerMoreTeacher.setOnClickListener {
//            startActivity(Intent(requireActivity(), TeacherSearchActivity::class.java))
//        }
    }

    private fun setNofiBtn() {
        binding.btnToolbarNotification.setOnClickListener {
            showNoti("제목", "본문") {
                // when confirm clicked
            }
        }
    }

    private fun setLectureRecyclerView() {

        lectureAdapter = LectureAdapter {}

//        binding.rvLecture.apply {
//            adapter = lectureAdapter
//            layoutManager =
//                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
//        }
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
            if (myProfileViewModel.amount.value!! < 100) {
                SimpleAlertDialog().apply {
                    title = "코인이 부족합니다"
                    description = "코인을 충전한 후 다시 질문해주세요"
                }.show(parentFragmentManager, "coin is insufficient")
            } else {
                startQuestionUploadActivity()
            }
        }
    }

    private fun startQuestionUploadActivity() {
        val intent = Intent(requireContext(), QuestionUploadActivity::class.java)
        startActivityForResult(intent, QUESTION_UPLOAD_RESULT)
    }

    private fun observeFollowing() {
        followingViewModel.following.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.containerMyTeacherSection.visibility = View.VISIBLE
                binding.nsTeacherFollowing.visibility = View.VISIBLE
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

    private fun observeTeachers() {
//        teacherRecommendViewModel.teacherRecommends.observe(viewLifecycleOwner) {
//            if (it.isNotEmpty()) {
//                binding.dvRanking.visibility = View.VISIBLE
//                binding.containerBestTeacherSection.visibility = View.VISIBLE
//            } else {
//                binding.dvRanking.visibility = View.GONE
//                binding.containerBestTeacherSection.visibility = View.GONE
//            }
//            teacherAdapter.setItem(it)
//            teacherAdapter.notifyDataSetChanged()
//        }
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
        observeTeachers()
        observeLectures()
        observeTeacherOnlines()
        observeEvents()
    }


    private fun observeLectures() {
//        lectureViewModel.lectures.observe(viewLifecycleOwner) {
//            if (it.isNotEmpty()) {
//                binding.tvLectureDesc.text = "지난 수업을 복습해봐요"
//                binding.rvLecture.visibility = View.VISIBLE
//                binding.containerLectureEmpty.visibility = View.GONE
//            } else {
//                binding.tvLectureDesc.text = "숏과외에 처음 오신 것을 환영해요!"
//                binding.rvLecture.visibility = View.GONE
//                binding.containerLectureEmpty.visibility = View.VISIBLE
//            }
//            lectureAdapter.setItem(it)
//            lectureAdapter.notifyDataSetChanged()
//        }
    }

    private fun observeTeacherOnlines() {
        teacherOnlineViewModel.teacherOnlines.observe(viewLifecycleOwner) {

            teacherOnlineAdapter.setItem(it)

            if (it.isNotEmpty()) {
                binding.containerMyTeacherSection.visibility = View.VISIBLE
                binding.nsTeacherOnline.visibility = View.VISIBLE
            } else {
                binding.dvTeacher.visibility = View.GONE
                binding.nsTeacherOnline.visibility = View.GONE
                if (followingViewModel.following.value.isNullOrEmpty()) {
                    binding.containerMyTeacherSection.visibility = View.GONE
                } else {
                    binding.containerMyTeacherSection.visibility = View.VISIBLE
                }
                teacherOnlineAdapter.notifyDataSetChanged()

            }
        }
    }

    private fun observeEvents() {
        eventViewModel.events.observe(viewLifecycleOwner) {
            it?.let {
                eventAdapter.setItems(it)
                eventAdapter.notifyDataSetChanged()
            }
        }
    }

    companion object {
        private val QUESTION_UPLOAD_RESULT = 1001
        val CLASSROOM_END_RESULT = 1002
    }
}