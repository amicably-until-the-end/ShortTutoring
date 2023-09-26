package org.softwaremaestro.presenter.chat_page.widget

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.question_upload.entity.TeacherVO
import org.softwaremaestro.presenter.chat_page.adapter.TeacherSelectAdapter
import org.softwaremaestro.presenter.databinding.DialogTeacherSelectBinding
import org.softwaremaestro.presenter.util.Util
import org.softwaremaestro.presenter.util.getVerticalSpaceDecoration
import kotlin.math.min

@AndroidEntryPoint
class AnsweringTeacherSelectDialog(questionId: String) :
    BottomSheetDialogFragment() {

    private lateinit var binding: DialogTeacherSelectBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogTeacherSelectBinding.inflate(layoutInflater)

        setTeacherRecyclerView()

        return binding.root
    }

    private fun setTeacherRecyclerView() {
        binding.rvTeacherList.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = TeacherSelectAdapter(
                teachers
            ) { teacherId -> Toast.makeText(context, teacherId, Toast.LENGTH_SHORT).show() }
            addItemDecoration(getVerticalSpaceDecoration(15, requireContext()))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            setupRatio(dialogInterface as BottomSheetDialog)
        }
        return dialog
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val behavior = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = min(
            Util.getBottomSheetDialogDefaultHeight(activity as Activity),
            layoutParams.height
        )
        bottomSheet?.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    companion object {
        private val teachers = listOf(
            TeacherVO(
                name = "팜하니 선생님",
                school = "서울대학교",
                likes = 10,
                teacherId = "teacher1",
                imageUrl = "https://chat.openai.com/_next/image?url=https%3A%2F%2Flh3.googleusercontent.com%2Fa%2FAAcHTtcaPO8nwFeU42FJpHZ6N7jkBX4_T6ziRAhKpwDC7eM4iQ%3Ds96-c&w=96&q=75",
            ),
            TeacherVO(
                name = "강해린 선생님",
                school = "서울대학교",
                likes = 10,
                teacherId = "teacher1",
                imageUrl = "https://chat.openai.com/_next/image?url=https%3A%2F%2Flh3.googleusercontent.com%2Fa%2FAAcHTtcaPO8nwFeU42FJpHZ6N7jkBX4_T6ziRAhKpwDC7eM4iQ%3Ds96-c&w=96&q=75",
            ),
        )

    }
}