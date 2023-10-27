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
import org.softwaremaestro.domain.socket.SocketManager
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentTeacherProfileBinding
import org.softwaremaestro.presenter.teacher_profile.viewmodel.FollowUserViewModel
import org.softwaremaestro.presenter.teacher_profile.viewmodel.ProfileViewModel
import org.softwaremaestro.presenter.util.Util
import org.softwaremaestro.presenter.util.toRating
import org.softwaremaestro.presenter.util.widget.DetailAlertDialog

class TeacherProfileFragment : Fragment() {

    private lateinit var binding: FragmentTeacherProfileBinding
    private lateinit var selectedUserId: String
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val followUserViewModel: FollowUserViewModel by activityViewModels()
    private lateinit var unfollowDialog: DetailAlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeacherProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUnfollowDialog()
        setProfile()
        observe()
    }

    private fun initUnfollowDialog() {
        unfollowDialog = DetailAlertDialog(
            title = "선생님 찜하기를 취소할까요?",
            description = "선생님에게 예약 질문을 할 수 없게 됩니다"
        ) {
            followUserViewModel.unfollowUser(selectedUserId)
            Toast.makeText(requireContext(), "선생님을 찜했습니다", Toast.LENGTH_SHORT).show()
        }
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
                unfollowDialog.show(parentFragmentManager, "unfollowDialog")
            } else {
                followUserViewModel.followUser(selectedUserId)
                Toast.makeText(requireContext(), "선생님 찜하기가 해제되었습니다", Toast.LENGTH_SHORT).show()
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
                it.name?.let { tvTeacherName.text = it }
                it.bio?.let { tvTeacherBio.text = it }
                it.schoolName?.let { tvTeacherUniv.text = it }
                it.schoolDepartment?.let { tvTeacherMajor.text = it }
                it.rating?.let { tvTeacherRating.text = it.toRating() }
                Glide.with(this@TeacherProfileFragment).load(it.profileImage).circleCrop()
                    .into(ivTeacherImg)
                btnFollow.text = "찜한 학생 ${it.followers?.size ?: 0}명"
                if (SocketManager.userId != null && it.followers != null) {
                    (SocketManager.userId!! in it.followers!!).also {
                        setBtnFollow(it)
                        setBtnReserve(it)
                    }
                }
            }
        }
    }

    private fun observeFollowUserState() {
        followUserViewModel.followUserState.observe(viewLifecycleOwner) {
            profileViewModel.getProfile(selectedUserId)
        }
    }

    companion object {
        private val QUESTION_UPLOAD_RESULT = 1001
    }
}