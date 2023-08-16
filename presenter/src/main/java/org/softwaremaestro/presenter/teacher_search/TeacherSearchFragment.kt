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
import org.softwaremaestro.presenter.databinding.FragmentTeacherSearchBinding
import org.softwaremaestro.presenter.student_home.StudentHomeFragmentDirections
import org.softwaremaestro.presenter.student_home.adapter.TeacherAdapter
import org.softwaremaestro.presenter.student_home.adapter.TeacherFollowingAdapter
import org.softwaremaestro.presenter.student_home.item.BestTeacher
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
    ): View? {
        binding = FragmentTeacherSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        myProfileViewModel.getMyProfile()

        setFollowingRecyclerView()
        setBestTeacherRecyclerView()

        setItemToBestTeacherAdapter()

        observeFollowing()
        observeMyProfile()


        super.onViewCreated(view, savedInstanceState)
    }

    private fun setFollowingRecyclerView() {
        teacherFollowingAdapter = TeacherFollowingAdapter {
            val action =
                StudentHomeFragmentDirections.actionStudentHomeFragmentToTeacherProfileFragment(it)
            findNavController().navigate(action)
        }

        binding.rvFollowing.apply {
            adapter = teacherFollowingAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setBestTeacherRecyclerView() {

        teacherAdapter = TeacherAdapter {}

        binding.rvBestTeacher.apply {
            adapter = teacherAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setItemToBestTeacherAdapter() {
        val lectures = mutableListOf<BestTeacher>().apply {
            add(
                BestTeacher(
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Circle-icons-profile.svg/2048px-Circle-icons-profile.svg.png",
                    "강해린",
                    "1",
                    35,
                    "성균관대학교",
                    4.9f
                )
            )
            (1..4).forEach {
                add(
                    BestTeacher(
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Circle-icons-profile.svg/2048px-Circle-icons-profile.svg.png",
                        "팜하니",
                        "1",
                        31,
                        "피식대학교",
                        4.8f
                    )
                )
            }
        }
        teacherAdapter.setItem(lectures)
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