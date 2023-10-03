package org.softwaremaestro.presenter.teacher_search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.teacher_get.entity.TeacherVO
import org.softwaremaestro.presenter.databinding.FragmentTeacherSearchBinding
import org.softwaremaestro.presenter.student_home.StudentHomeFragmentDirections
import org.softwaremaestro.presenter.student_home.adapter.TeacherFollowingAdapter
import org.softwaremaestro.presenter.teacher_search.adapter.TeacherAdapter
import org.softwaremaestro.presenter.teacher_search.viewmodel.FollowingViewModel
import org.softwaremaestro.presenter.teacher_search.viewmodel.MyProfileViewModel

@AndroidEntryPoint
class TeacherSearchFragment : Fragment() {

    private lateinit var binding: FragmentTeacherSearchBinding

    private val followingViewModel: FollowingViewModel by viewModels()
    private val myProfileViewModel: MyProfileViewModel by viewModels()

    private lateinit var teacherFollowingAdapter: TeacherFollowingAdapter
    private lateinit var teacherAdapter: TeacherAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeacherSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        myProfileViewModel.getMyProfile()

        setFollowingRecyclerView()
        setTeacherRecommendRecyclerView()

        setItemToTeacherAdapter()

        observeFollowing()
        observeMyProfile()


        super.onViewCreated(view, savedInstanceState)
    }

    private fun setFollowingRecyclerView() {
        teacherFollowingAdapter = TeacherFollowingAdapter {
            //TODO: 선생님별
            val action =
                StudentHomeFragmentDirections.actionStudentHomeFragmentToTeacherProfileFragment("null")
            findNavController().navigate(action)
        }

        binding.rvFollowing.apply {
            adapter = teacherFollowingAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setTeacherRecommendRecyclerView() {

        teacherAdapter = TeacherAdapter {}

        binding.rvTeacherRecommend.apply {
            adapter = teacherAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setItemToTeacherAdapter() {
        val teachers = mutableListOf<TeacherVO>().apply {
            add(
                TeacherVO(
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Circle-icons-profile.svg/2048px-Circle-icons-profile.svg.png",
                    "강해린",
                    "1",
                    "풀 수 없는 문제는 없다.",
                    "성균관대학교",
                    -2f,
                    listOf(),
                    -1
                )
            )
        }
//        teacherAdapter.setItem(teachers)
    }

    private fun observeFollowing() {
        followingViewModel.following.observe(viewLifecycleOwner) {
            teacherFollowingAdapter.setItem(it)
            teacherFollowingAdapter.notifyDataSetChanged()
        }
    }

    private fun observeMyProfile() {
        myProfileViewModel.myProfile.observe(viewLifecycleOwner) {

            if (it.id == null) {
                // 에러 처리
            } else {
                followingViewModel.getFollowing(it.id!!)
            }
        }
    }
}