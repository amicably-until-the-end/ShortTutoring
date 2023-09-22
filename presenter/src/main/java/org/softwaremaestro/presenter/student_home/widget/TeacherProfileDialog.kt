package org.softwaremaestro.presenter.student_home.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.teacher_get.entity.TeacherVO
import org.softwaremaestro.presenter.databinding.DialogTeacherProfileBinding

@AndroidEntryPoint
class TeacherProfileDialog(
    private val onFollowBtnClicked: () -> Unit,
    private val onReserveBtnClicked: () -> Unit,
) :
    BottomSheetDialogFragment() {

    private lateinit var binding: DialogTeacherProfileBinding
    lateinit var item: TeacherVO

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogTeacherProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFollowBtn()
        setReserveBtn()
        bind()
    }

    private fun bind() {
        with(binding) {
            Glide.with(root.context).load(item.profileUrl).centerCrop()
                .into(ivTeacherImg)
            tvTeacherName.text = item.nickname
            tvTeacherUniv.text = item.univ
            tvTeacherBio.text = item.bio
            tvTeacherRating.text = item.rating.toString()
            btnFollow.text = "찜한 학생 ${item.pickCount}"
            // Todo: 예약 질문 갯수
        }
    }

    private fun setFollowBtn() {
        binding.btnFollow.setOnClickListener { onFollowBtnClicked() }
    }

    private fun setReserveBtn() {
        binding.containerReserve.setOnClickListener { onReserveBtnClicked() }
    }
}