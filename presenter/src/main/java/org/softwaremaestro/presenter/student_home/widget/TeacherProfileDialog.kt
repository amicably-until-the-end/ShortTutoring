package org.softwaremaestro.presenter.student_home.widget

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.best_teacher_get.entity.TeacherVO
import org.softwaremaestro.domain.review.entity.ReviewResVO
import org.softwaremaestro.domain.socket.SocketManager
import org.softwaremaestro.domain.tutoring_get.entity.TutoringVO
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.DialogTeacherProfileBinding
import org.softwaremaestro.presenter.student_home.adapter.LectureAdapter
import org.softwaremaestro.presenter.teacher_home.adapter.ReviewAdapter
import org.softwaremaestro.presenter.util.Util
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

    private lateinit var tutoringAdapter: LectureAdapter
    private lateinit var reviewAdapter: ReviewAdapter

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
        setRecyclerViews()
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
        item.teacherId ?: return
        mItem = item
        following = SocketManager.userId in item.followers!!
        followerCnt = item.followers!!.size
    }

    fun setItemToReviewRecyclerView(reviews: List<ReviewResVO>) {
        if (reviews.isEmpty()) {
            binding.containerReviewAndTutoring.visibility = View.GONE
        }
        binding.tvNumOfReview.text = reviews.size.toString()
        reviewAdapter.setItem(reviews)
        reviewAdapter.notifyDataSetChanged()
    }

    fun setItemToTutoringRecyclerView(tutorings: List<TutoringVO>) {
        tutoringAdapter.setItem(tutorings)
        tutoringAdapter.notifyDataSetChanged()
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

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            super.show(manager, tag)
        } catch (_: Exception) {
        }
    }

    private fun setRecyclerViews() {
        setTutoringRecyclerView()
        setReviewRecyclerView()
    }

    private fun setTutoringRecyclerView() {
        tutoringAdapter = LectureAdapter {
            // 과외 영상 보여주기
        }

        with(binding.rvClip) {
            adapter = tutoringAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setReviewRecyclerView() {
        reviewAdapter = ReviewAdapter()

        with(binding.rvReview) {
            adapter = reviewAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setProfileContainer() {
        binding.cvInfoBox.setOnClickListener {
            mItem.teacherId?.let { onProfileClick(it) }
        }
    }

    private fun setFollowBtn() {
        mItem.teacherId ?: run {
            Util.createToast(requireActivity(), "선생님의 아이디가 확인되지 않습니다")
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
                    Util.createToast(requireActivity(), "선생님 아이디를 가져오는데 실패했습니다")
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