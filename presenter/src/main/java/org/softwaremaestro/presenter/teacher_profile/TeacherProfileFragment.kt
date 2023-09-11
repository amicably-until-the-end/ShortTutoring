package org.softwaremaestro.presenter.teacher_profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.user_follow.SUCCESS_USER_FOLLOW
import org.softwaremaestro.domain.user_follow.SUCCESS_USER_UNFOLLOW
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.util.decreaseWidth
import org.softwaremaestro.presenter.util.increaseWidth
import org.softwaremaestro.presenter.databinding.FragmentTeacherProfileBinding
import org.softwaremaestro.presenter.teacher_profile.viewmodel.NotiFollowUserViewModel
import org.softwaremaestro.presenter.teacher_profile.viewmodel.ProfileViewModel

private const val RATING = 4.83333f
private const val NUM_OF_CLIP = 23

@AndroidEntryPoint
class TeacherProfileFragment : Fragment() {

    private lateinit var binding: FragmentTeacherProfileBinding

    private val args by navArgs<TeacherProfileFragmentArgs>()
    private lateinit var selectedUserId: String

    private var following = true

    private val profileViewModel: ProfileViewModel by viewModels()
    private val notiFollowUserViewModel: NotiFollowUserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (args.teacherId == null) {
            // 에러 처리
        } else {
            selectedUserId = args.teacherId!!
        }

        // following 설정

        binding = FragmentTeacherProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        profileViewModel.getProfile(selectedUserId)

        observe()

        binding.btnFollow.setOnClickListener {
            toggleFollowing()
        }
    }

    private fun observe() {

        observeProfile()

        observeNumOfFollower()

        observeNotiFollowUser()

        observeNotiUnfollowUser()
    }

    private fun observeProfile() {
        profileViewModel.profile.observe(viewLifecycleOwner) {

            with(binding) {
                Glide.with(requireContext()).load(it.profileImage).circleCrop().into(ivTeacherImg)
                tvTeacherSchool.text = "${it.schoolName} ${it.schoolDepartment}"
                tvTeacherName.text = it.name
                tvRating.text = "%.2f".format(RATING)
                tvNumOfClip.text = NUM_OF_CLIP.toString()
            }
        }
    }

    private fun observeNumOfFollower() {
        profileViewModel.numOfFollower.observe(viewLifecycleOwner) {
            binding.btnFollow.text = "찜하기 $it"
        }
    }

    private fun observeNotiFollowUser() {
        notiFollowUserViewModel.notiFollowUser.observe(viewLifecycleOwner) {
            if (it != SUCCESS_USER_FOLLOW) {
                Log.d("error", "failed in following user")
            }
        }
    }

    private fun observeNotiUnfollowUser() {
        notiFollowUserViewModel.notiUnfollowUser.observe(viewLifecycleOwner) {
            if (it != SUCCESS_USER_UNFOLLOW) {
                Log.d("error", "failed in umfollowing user")
            }
        }
    }

    private fun toggleFollowing() {
        following = !following

        if (following) {
            binding.btnFollow.setBackgroundResource(R.drawable.bg_radius_10_dark_grey)
            profileViewModel.addOne()

            binding.btnFollow.decreaseWidth(156, 500L, requireContext(),
                onEnd = { binding.btnReserve.visibility = View.VISIBLE }
            )

            notiFollowUserViewModel.followUser(selectedUserId)

        } else {
            binding.btnFollow.setBackgroundResource(R.drawable.bg_radius_10_blue)
            profileViewModel.minusOne()

            binding.btnFollow.increaseWidth(156, 500L, requireContext(),
                onStart = { binding.btnReserve.visibility = View.GONE }
            )

            notiFollowUserViewModel.unfollowUser(selectedUserId)
        }
    }
}