package org.softwaremaestro.presenter.my_page.student_my_page

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.databinding.FragmentStudentMyPageBinding
import org.softwaremaestro.presenter.my_page.viewmodel.FollowerViewModel
import org.softwaremaestro.presenter.my_page.viewmodel.LecturesViewModel
import org.softwaremaestro.presenter.my_page.viewmodel.ProfileViewModel
import org.softwaremaestro.presenter.student_home.adapter.LectureAdapter
import org.softwaremaestro.presenter.teacher_home.adapter.ReviewAdapter
import org.softwaremaestro.presenter.util.widget.ProfileImageSelectBottomDialog

@AndroidEntryPoint
class StudentMyPageFragment : Fragment() {

    private lateinit var binding: FragmentStudentMyPageBinding

    private val lecturesViewModel: LecturesViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val followerViewModel: FollowerViewModel by viewModels()

    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var lectureAdapter: LectureAdapter

    private lateinit var dialog: ProfileImageSelectBottomDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentMyPageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.getMyProfile()
        initLectureRecyclerView()

        setBtnEditTeacherImg()
        setFollowingMenu()
        setServiceCenterMenu()

        observe()
    }

    private fun observe() {
        observeLectures()
        observeProfile()
    }

    private fun observeLectures() {
//        lecturesViewModel.lectures.observe(requireActivity()) {
//            binding.containerClipEmpty.visibility =
//                if (it.isEmpty()) View.VISIBLE else View.GONE
//
//            lectureAdapter.apply {
//                setItem(it)
//                notifyDataSetChanged()
//            }
//
//            binding.tvNumOfClip.text = it.size.toString()
//        }
    }

    private fun observeProfile() {

        profileViewModel.name.observe(viewLifecycleOwner) {
            binding.tvStudentName.text = it
        }

        profileViewModel.schoolLevel.observe(viewLifecycleOwner) {
            binding.tvStudentSchoolLevel.text = it
        }

        profileViewModel.schoolGrade.observe(viewLifecycleOwner) {
            binding.tvStudentSchoolGrade.text = "${it}학년"
        }

        profileViewModel.image.observe(viewLifecycleOwner) {
            Glide.with(requireContext()).load(it).circleCrop().into(binding.ivStudentImg)
        }

        profileViewModel.followers.observe(viewLifecycleOwner) {
            binding.btnFollow.text = "찜한 선생님 ${it.size}명"
        }
    }

    private fun initLectureRecyclerView() {

        lectureAdapter = LectureAdapter {
            // url을 이용해 영상 재생
            it
        }

//        binding.rvClip.apply {
//            adapter = lectureAdapter
//            layoutManager =
//                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
//        }

        lecturesViewModel.getLectures()
    }

    private fun setBtnEditTeacherImg() {
//        binding.containerStudentImg.setOnClickListener {
//            dialog = ProfileImageSelectBottomDialog(
//                onImageChanged = { image ->
//                    binding.ivStudentImg.setBackgroundResource(image)
//                },
//                onSelect = { res ->
//                    val image = BitmapFactory.decodeResource(resources, res).toBase64()
//                    profileViewModel.setImage(image)
////                    profileViewModel.updateProfile()
//                    dialog.dismiss()
//                },
//            )
//            dialog.show(parentFragmentManager, "profileImageSelectBottomDialog")
//        }
    }

    private fun setFollowingMenu() {
        binding.containerFollowing.setOnClickListener {
            startActivity(Intent(requireActivity(), FollowingActivity::class.java))
        }
    }

    private fun setServiceCenterMenu() {
        binding.containerServiceCenter.setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                addCategory(Intent.CATEGORY_BROWSABLE)
                data = Uri.parse(SERVICE_CENTER_URL)
            }
            startActivity(intent)
        }
    }

    companion object {
        private const val SERVICE_CENTER_URL = "https://www.form.short-tutoring.com"
    }
}
