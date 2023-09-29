package org.softwaremaestro.presenter.teacher_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentTeacherProfileBinding
import org.softwaremaestro.presenter.teacher_profile.viewmodel.FollowUserViewModel
import org.softwaremaestro.presenter.teacher_profile.viewmodel.ProfileViewModel
import org.softwaremaestro.presenter.util.Util

class TeacherProfileFragment : Fragment() {

    private lateinit var binding: FragmentTeacherProfileBinding

    private lateinit var selectedUserId: String

    private var following = true
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val followUserViewModel: FollowUserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTeacherProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProfile()
        observe()
        setBtnFollow()
        setBtnReserve()
    }

    private fun setProfile() {
        if (requireActivity().intent.getStringExtra("teacher-id") != null) {
            requireActivity().intent.getStringExtra("teacher-id")!!.let {
                selectedUserId = it
                profileViewModel.getProfile(it)
            }
        } else {
            Util.logError(this@TeacherProfileFragment::class.java, "teacher id is null")
        }
    }

    private fun setBtnFollow() {
        binding.btnFollow.setOnClickListener {
            if (following) {
                followUserViewModel.unfollowUser(selectedUserId)
                Toast.makeText(requireContext(), "선생님 찜하기가 해제되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                followUserViewModel.followUser(selectedUserId)
                Toast.makeText(requireContext(), "선생님을 찜했습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setBtnReserve() {
        binding.containerReserve.setOnClickListener {
            if (following)
                findNavController().navigate(R.id.action_teacherProfileFragment_to_reservationFormFragment)
            else
                Toast.makeText(requireContext(), "예약 질문은 찜한 선생님에게만 할 수 있어요.", Toast.LENGTH_SHORT)
                    .show()
        }
    }

    private fun observe() {
        observeProfile()
        observeFollowUserState()
    }

    private fun observeProfile() {
        profileViewModel.profile.observe(requireActivity()) {
            with(binding) {
                tvTeacherName.text = it.name
                tvTeacherBio.text = it.bio
                tvTeacherUniv.text = it.schoolName
                tvTeacherMajor.text = it.schoolDepartment
//                tvTeacherRating.text = it.rating
                Glide.with(this@TeacherProfileFragment).load(it.profileImage).circleCrop()
                    .into(ivTeacherImg)
                btnFollow.text = "찜한 사람 ${it.followersCount}명"
//                following = (studentId in it.followers).let {
//                    if (it) {
//                        binding.btnFollow.setBackgroundResource(R.drawable.bg_radius_10_dark_grey)
//                    } else {
//                        binding.btnFollow.setBackgroundResource(R.drawable.bg_radius_10_grad_blue)
//
//                    }
//                }
            }
        }
    }

    private fun observeFollowUserState() {
        followUserViewModel.followUserState.observe(requireActivity()) {
            profileViewModel.getProfile(selectedUserId)
        }
    }

    companion object {
        private val QUESTION_UPLOAD_RESULT = 1001
    }
}