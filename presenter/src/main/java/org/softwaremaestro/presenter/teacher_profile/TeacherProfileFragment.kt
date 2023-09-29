package org.softwaremaestro.presenter.teacher_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.databinding.FragmentTeacherProfileBinding
import org.softwaremaestro.presenter.teacher_profile.viewmodel.FollowUserViewModel
import org.softwaremaestro.presenter.teacher_profile.viewmodel.ProfileViewModel
import org.softwaremaestro.presenter.util.Util.logError

@AndroidEntryPoint
class TeacherProfileFragment : Fragment() {

    private lateinit var binding: FragmentTeacherProfileBinding

    private val args by navArgs<TeacherProfileFragmentArgs>()
    private lateinit var selectedUserId: String

    private var following = false
    private val profileViewModel: ProfileViewModel by viewModels()
    private val followUserViewModel: FollowUserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (args.teacherId != null) {
            args.teacherId!!.let {
                selectedUserId = it
                profileViewModel.getProfile(it)
            }
        } else {
            logError(this@TeacherProfileFragment::class.java, "teacher id is null")
        }

        binding = FragmentTeacherProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()
        setBtnFollow()
    }

    private fun setBtnFollow() {
        binding.btnFollow.setOnClickListener {
            if (following)
                followUserViewModel.unfollowUser(selectedUserId)
            else
                followUserViewModel.followUser(selectedUserId)
        }
    }

    private fun observe() {
        observeProfile()
        observeFollowUserState()
    }

    private fun observeProfile() {
        profileViewModel.profile.observe(viewLifecycleOwner) {
            with(binding) {
                tvTeacherName.text = it.name
                tvTeacherBio.text = it.bio
                tvTeacherUniv.text = it.schoolName
                tvTeacherMajor.text = it.schoolDepartment
//                tvTeacherRating.text = it.rating
                Glide.with(requireContext()).load(it.profileImage).circleCrop().into(ivTeacherImg)
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
        followUserViewModel.followUserState.observe(viewLifecycleOwner) {
            profileViewModel.getProfile(selectedUserId)
        }
    }
}