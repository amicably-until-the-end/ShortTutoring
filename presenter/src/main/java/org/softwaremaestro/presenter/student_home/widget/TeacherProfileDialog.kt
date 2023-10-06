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
import org.softwaremaestro.presenter.util.widget.DetailAlertDialog

@AndroidEntryPoint
class TeacherProfileDialog(
    private val onProfileClick: (String) -> Unit,
    private val onUnfollow: (String) -> Unit,
    private val onFollow: (String) -> Unit,
    private val onReserve: (String) -> Unit,
) :
    BottomSheetDialogFragment() {

    private lateinit var binding: DialogTeacherProfileBinding
    private lateinit var mItem: TeacherVO
    private var following = false
    private lateinit var unfollowDialog: DetailAlertDialog

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
        initUnfollowDialog()
        setProfileContainer()
        setFollowBtn()
        setReserveBtn()
        bind()
    }

    /**
    this method initializes the property mItem.
    this MUST be called when creating this dialog.
     */
    fun setItem(item: TeacherVO) {
        if (SocketManager.userId == null || item.followers == null) return
        mItem = item
        following = SocketManager.userId!! in item.followers!!
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
            mItem.teacherId?.let { onProfileClick(it) }
        }
    }

    private fun setFollowBtn() {
        with(mItem) {
            if (teacherId != null) {
                binding.btnFollow.setOnClickListener {
                    if (following) {
                        unfollowDialog.show(parentFragmentManager, "unfollowDialog")
                    } else {
                        following = true
                        onFollow(teacherId!!)
                        with(binding.btnFollow) {
                            setBackgroundResource(R.drawable.bg_radius_5_background_light_blue)
                            setTextColor(resources.getColor(R.color.primary_blue, null))
                            mItem.followers?.let { text = "찜한 학생 ${it.size + 1}" }
                        }
                    }
                }
            } else {
                // 에러 처리
                Toast.makeText(requireContext(), "선생님의 아이디가 확인되지 않습니다", Toast.LENGTH_SHORT)
                logError(
                    this@TeacherProfileDialog::class.java,
                    "teacherId == null in setFollowBtn()"
                )
            }
        }
    }

    private fun setReserveBtn() {
        binding.containerReserve.setOnClickListener {
            mItem.teacherId?.let { onReserve(it) }
        }
    }

    private fun initUnfollowDialog() {
        unfollowDialog = DetailAlertDialog(
            title = "선생님 찜하기를 취소할까요?",
            description = "선생님에게 예약 질문을 할 수 없게 됩니다"
        ) {
            onUnfollow(mItem.teacherId!!)
            following = false
            with(binding.btnFollow) {
                setBackgroundResource(R.drawable.bg_radius_5_grad_blue)
                setTextColor(resources.getColor(R.color.white, null))
                mItem.followers?.let { text = "찜한 학생 ${it.size}" }
            }
        }
    }
}