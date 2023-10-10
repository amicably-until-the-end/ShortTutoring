package org.softwaremaestro.presenter.classroom

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import org.softwaremaestro.presenter.databinding.DialogRatingBinding

private const val NUM_STAR = 5

class reviewDialog(
    private val teacherName: String,
    private val onConfirm: (Int, String) -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogRatingBinding

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvMain.text = "${teacherName} 선생님과의 수업은 어땠나요?"
        binding.srbRating.setOnClickListener {
            if (binding.srbRating.numOfFilled >= 4) {
                binding.containerReview.visibility = View.VISIBLE
            } else {
                binding.containerReview.visibility = View.GONE
            }
        }
        binding.containerLeaveReview.setOnClickListener {
            onConfirm(binding.srbRating.numOfFilled, binding.etReview.text?.toString() ?: "")
        }
    }
}