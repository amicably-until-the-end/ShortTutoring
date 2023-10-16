package org.softwaremaestro.presenter.classroom

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import org.softwaremaestro.presenter.databinding.DialogRatingBinding
import org.softwaremaestro.presenter.util.hideKeyboardAndRemoveFocus
import org.softwaremaestro.presenter.util.showKeyboardAndRequestFocus

private const val NUM_STAR = 5

class ReviewDialog(
    private val teacherName: String?,
    private val teacherImg: String?,
    private val onConfirm: (Int, String) -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogRatingBinding
    private var mNumOfFilled = 5

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogRatingBinding.inflate(layoutInflater)

        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestFeature(Window.FEATURE_NO_TITLE)
        }
        dialog?.apply {
            // prevent closing by outside click
            setCanceledOnTouchOutside(false)
            // prevent closing by back button
            setCancelable(false)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTeacherInfo()
        setSimpleRatingBar()
        setLeaveReviewContainer()
    }

    private fun setTeacherInfo() {
        binding.tvMain.text = "${teacherName ?: ""} 선생님과의 수업은 어땠나요?"
        Glide.with(binding.root).load(teacherImg).centerCrop().into(binding.ivTeacherImg)
    }

    private fun setSimpleRatingBar() {
        binding.containerReview.setOnClickListener {
            showKeyboardAndRequestFocus(binding.srbRating)
        }
        binding.srbRating.onClick = { numOfFilled ->
            mNumOfFilled = numOfFilled + 1
            if (numOfFilled >= 3) {
                binding.containerReview.visibility = View.VISIBLE
            } else {
                binding.containerReview.visibility = View.GONE
                hideKeyboardAndRemoveFocus(binding.srbRating)
            }
            binding.tvRating.text = ratingTexts[numOfFilled]
        }
    }

    private fun setLeaveReviewContainer() {
        binding.containerLeaveReview.setOnClickListener {
            onConfirm(mNumOfFilled, binding.etReview.text?.toString() ?: "")
            dismiss()
        }
    }

    companion object {
        private val ratingTexts = arrayOf(
            "다시 만나고 싶지 않아요",
            "이해가 잘 안 됐어요",
            "몰랐던 내용이 해결됐어요",
            "이해가 잘 됐어요",
            "다음에 또 질문하고 싶어요!"
        )
    }
}