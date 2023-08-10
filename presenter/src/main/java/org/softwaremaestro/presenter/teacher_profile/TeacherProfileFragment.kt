package org.softwaremaestro.presenter.teacher_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.Util.decreaseWidth
import org.softwaremaestro.presenter.Util.increaseWidth
import org.softwaremaestro.presenter.databinding.FragmentTeacherProfileBinding
import org.softwaremaestro.presenter.teacher_profile.viewmodel.ProfileViewModel

private const val RATING = 4.83333f
private const val NUM_OF_CLIP = 23

@AndroidEntryPoint
class TeacherProfileFragment : Fragment() {

    private lateinit var binding: FragmentTeacherProfileBinding

    private val args by navArgs<TeacherProfileFragmentArgs>()

    private val profileViewModel: ProfileViewModel by viewModels()

    private var following = true

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

        binding.btnFollow.setOnClickListener {
            toggleFollowing()
        }
    }

    private fun setProfile() {

        args.teacherId?.let {
            profileViewModel.getProfile(it)
        }

        profileViewModel.profile.observe(viewLifecycleOwner) {

            with(binding) {
                Picasso.with(requireContext()).load(it.profileImage).fit().centerCrop()
                    .into(ivTeacherImg)
                tvTeacherSchool.text = "${it.schoolName} ${it.schoolDepartment}"
                tvTeacherName.text = it.name
                tvRating.text = "%.2f".format(RATING)
                tvNumOfClip.text = NUM_OF_CLIP.toString()
            }
        }

        profileViewModel.numOfFollower.observe(viewLifecycleOwner) {
            binding.btnFollow.text = "찜하기 $it"
        }
    }

    private fun toggleFollowing() {
        following = !following

        if (following) {
            binding.btnFollow.setBackgroundResource(R.drawable.btn_corner_radius_10_disabled)
            profileViewModel.addOne()

            binding.btnFollow.decreaseWidth(156, 500L, requireContext(),
                onEnd = { binding.btnReserve.visibility = View.VISIBLE }
            )
        } else {
            binding.btnFollow.setBackgroundResource(R.drawable.btn_corner_radius_10_enabled)
            profileViewModel.minusOne()

            binding.btnFollow.increaseWidth(156, 500L, requireContext(),
                onStart = { binding.btnReserve.visibility = View.GONE }
            )
        }
    }
}