package org.softwaremaestro.presenter.student_home.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.socket.SocketManager
import org.softwaremaestro.domain.teacher_get.entity.TeacherVO
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.DialogTeacherProfileBinding
import org.softwaremaestro.presenter.util.Util.logError

@AndroidEntryPoint
class TeacherProfileDialog(
    private val onProfileClicked: (String) -> Unit,
    private val onFollowBtnClicked: (Boolean, String) -> Unit,
    private val onReserveBtnClicked: (String) -> Unit,
) :
    BottomSheetDialogFragment() {

    private lateinit var binding: DialogTeacherProfileBinding
    private lateinit var mItem: TeacherVO
    private var following = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogTeacherProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setProfileContainer()
        setFollowBtn()
        setReserveBtn()
        bind()
    }

    private fun bind() {
        with(binding) {
            Glide.with(root.context).load(mItem.profileUrl).centerCrop()
                .into(ivTeacherImg)
            tvTeacherName.text = mItem.nickname
            tvTeacherUniv.text = mItem.univ
            tvTeacherBio.text = mItem.bio
            tvTeacherRating.text = mItem.rating.toString()
            // Todo: 추후에 api로 수정
            btnFollow.text = "찜한 학생 ${mItem.followers?.size ?: 0}"
            tvReservationCnt.text = "${mItem.reservationCnt ?: 0}"
        }
    }

    private fun setProfileContainer() {
        binding.containerContent.setOnClickListener {
            mItem.teacherId?.let { onProfileClicked(it) }
        }
    }

    private fun setFollowBtn() {
        with(mItem) {
            if (teacherId == null) {
                Toast.makeText(requireContext(), "선생님의 아이디가 확인되지 않습니다", Toast.LENGTH_SHORT)
                logError(
                    this@TeacherProfileDialog::class.java,
                    "teacherId == null in setFollowBtn()"
                )
            } else {
                binding.btnFollow.setOnClickListener {
                    toggleFollow()
                    onFollowBtnClicked(
                        following, teacherId!!
                    )
                }
            }
        }
    }

    private fun setReserveBtn() {
        binding.containerReserve.setOnClickListener {
            mItem.teacherId?.let { onReserveBtnClicked(it) }
        }
    }

    fun setItem(item: TeacherVO) {
        if (SocketManager.userId == null || item.followers == null) return
        mItem = item
        following = SocketManager.userId!! in item.followers!!
    }

    /**
    toggle value 'following' and changes the layout of followBtn upon the value
     */
    fun toggleFollow() {
        following = !following
        with(binding.btnFollow) {
            if (following) {
                setBackgroundResource(R.drawable.bg_radius_5_background_light_blue)
                setTextColor(resources.getColor(R.color.primary_blue, null))
                mItem.followers?.let { text = "찜한 학생 ${it.size + 1}" }

            } else {
                setBackgroundResource(R.drawable.bg_radius_5_grad_blue)
                setTextColor(resources.getColor(R.color.white, null))
                mItem.followers?.let { text = "찜한 학생 ${it.size}" }
            }
        }
    }
}