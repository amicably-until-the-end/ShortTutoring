package org.softwaremaestro.presenter.student_home.widget

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.following_get.entity.FollowingGetResponseVO
import org.softwaremaestro.presenter.chat_page.adapter.TeacherSelectAdapter
import org.softwaremaestro.presenter.databinding.DialogTeacherProfileBinding

@AndroidEntryPoint
class TeacherProfileDialog(private val teacherVO: FollowingGetResponseVO) :
    BottomSheetDialogFragment() {

    private lateinit var binding: DialogTeacherProfileBinding

    private lateinit var teacherSelectAdapter: TeacherSelectAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogTeacherProfileBinding.inflate(layoutInflater)

        setContentsRecyclerView()
        setTeacherInfo()

        return binding.root
    }

    private fun setTeacherInfo() {
        Glide.with(binding.root.context).load(teacherVO.profileImage).centerCrop()
            .into(binding.ivTeacherImg)
        binding.tvTeacherName.text = teacherVO.name
        binding.tvTeacherUniv.text = "undefined undefined"
    }

    private fun setContentsRecyclerView() {
        //TODO: set contents recyclerview
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState)


        return dialog
    }

}