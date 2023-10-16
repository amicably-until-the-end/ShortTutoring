package org.softwaremaestro.presenter.student_home.widget

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
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
import org.softwaremaestro.presenter.util.toRating
import org.softwaremaestro.presenter.util.widget.DetailAlertDialog

@AndroidEntryPoint
class TeacherProfileDialog(
    private val onProfileClick: (String) -> Unit,
    private val onUnfollow: (String) -> Unit,
    private val onFollow: (String) -> Unit,
    private val onReserve: (String) -> Unit,
    private val onDismiss: () -> Unit,
) :
    BottomSheetDialogFragment() {

    private lateinit var binding: DialogTeacherProfileBinding
    private lateinit var mItem: TeacherVO
    private var following = false
    private var followerCnt = 0
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismiss()
    }

    /**
    this method initializes the property mItem.
    this MUST be called when creating this dialog.
     */
    fun setItem(item: TeacherVO) {
        SocketManager.userId ?: return
        item.followers ?: return
        mItem = item
        following = SocketManager.userId in item.followers!!
        followerCnt = item.followers!!.size
    }

    private fun bind() {
        with(binding) {
            Glide.with(root.context).load(mItem.profileUrl).centerCrop()
                .into(ivTeacherImg)
            mItem.nickname?.let { tvTeacherName.text = it }
            tvTeacherUniv.text = "${mItem.univ} ${mItem.major}"
            mItem.bio?.let { tvTeacherBio.text = it }
            mItem.rating?.let { tvTeacherRating.text = it.toRating() }
            btnFollow.text = "찜한 학생 ${followerCnt}"
            tvReservationCnt.text = "${mItem.reservationCnt ?: 0}"
        }
    }

    private fun setProfileContainer() {
        binding.containerContent.setOnClickListener {
            mItem.teacherId?.let { onProfileClick(it) }
        }
    }

    private fun setFollowBtn() {
        mItem.teacherId ?: run {
            Toast.makeText(requireContext(), "선생님의 아이디가 확인되지 않습니다", Toast.LENGTH_SHORT)
            logError(
                this@TeacherProfileDialog::class.java,
                "teacherId == null in setFollowBtn()"
            )
        }

        if (following) {
            with(binding.btnFollow) {
                Log.d("hhcc", "following and ui changed")
                setBackgroundResource(R.drawable.bg_radius_5_background_light_blue)
                setTextColor(resources.getColor(R.color.primary_blue, null))
            }
        }

        binding.btnFollow.setOnClickListener {
            if (following) {
                unfollowDialog.show(parentFragmentManager, "unfollowDialog")
            } else {
                mItem.teacherId ?: run {
                    Toast.makeText(requireContext(), "선생님 아이디를 가져오는데 실패했습니다", Toast.LENGTH_SHORT)
                        .show()
                }

                following = true
                onFollow(mItem.teacherId!!)
                with(binding.btnFollow) {
                    setBackgroundResource(R.drawable.bg_radius_5_background_light_blue)
                    setTextColor(resources.getColor(R.color.primary_blue, null))
                    followerCnt++
                    mItem.followers?.let { text = "찜한 학생 ${followerCnt}" }
                }
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
            onUnfollow(mItem.teacherId ?: "undefined")
            following = false
            with(binding.btnFollow) {
                setBackgroundResource(R.drawable.bg_radius_5_grad_blue)
                setTextColor(resources.getColor(R.color.white, null))
                followerCnt--
                mItem.followers?.let { text = "찜한 학생 ${followerCnt}" }
            }
        }
    }
}