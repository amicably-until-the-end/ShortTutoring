package org.softwaremaestro.presenter.teacher_profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import org.softwaremaestro.domain.socket.SocketManager
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentTeacherProfileBinding
import org.softwaremaestro.presenter.teacher_profile.viewmodel.FollowUserViewModel
import org.softwaremaestro.presenter.teacher_profile.viewmodel.ProfileViewModel
import org.softwaremaestro.presenter.util.Util

class TeacherProfileFragment : Fragment() {

    private lateinit var binding: FragmentTeacherProfileBinding
    private lateinit var selectedUserId: String
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val followUserViewModel: FollowUserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeacherProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProfile()
        observe()
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

    /**
    changes the layout of followBtn and the click listener upon the value of following
     */
    private fun setBtnFollow(following: Boolean) {
        with(binding.btnFollow) {
            if (following) {
                setBackgroundResource(R.drawable.bg_radius_5_background_light_blue)
                setTextColor(resources.getColor(R.color.primary_blue, null))
            } else {
                setBackgroundResource(R.drawable.bg_radius_5_grad_blue)
                setTextColor(resources.getColor(R.color.white, null))
            }
        }

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

    private fun setBtnReserve(following: Boolean) {
        binding.containerReserve.setOnClickListener {
            if (following)
                findNavController().navigate(R.id.action_teacherProfileFragment_to_reservationFormFragment)
            else
                Toast.makeText(requireContext(), "예약 질문은 찜한 선생님에게만 할 수 있어요", Toast.LENGTH_SHORT)
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
                // Todo: api에서 가져오기
//                tvTeacherRating.text = it.rating
                Glide.with(this@TeacherProfileFragment).load(it.profileImage).circleCrop()
                    .into(ivTeacherImg)
                btnFollow.text = "찜한 사람 ${it.followersCount}명"
                // Todo: api에서 가져오기
                val followers = listOf<String>(SocketManager.userId!!)
                if (SocketManager.userId != null && followers != null) {
                    (SocketManager.userId!! in followers).also {
                        Log.d("hhcc", "following : ${it}")
                        setBtnFollow(it)
                        setBtnReserve(it)
                    }
                }
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