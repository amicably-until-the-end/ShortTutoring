package org.softwaremaestro.presenter.classroom

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.DialogRatingBinding

private const val NUM_STAR = 5

class RatingDialog : DialogFragment() {

    private lateinit var binding: DialogRatingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogRatingBinding.inflate(layoutInflater)

        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestFeature(Window.FEATURE_NO_TITLE)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStars()
    }

    private fun setStars() {

        (0 until NUM_STAR).forEach {
            binding.containerStar.getChildAt(it).setOnClickListener { clicked ->

                val idxOfClicked = (0 until NUM_STAR).map {
                    Pair(it, binding.containerStar.getChildAt(it).id)
                }.find {
                    it.second == clicked.id
                }?.first

                if (idxOfClicked != null) {
                    repeat(NUM_STAR) {
                        binding.containerStar.getChildAt(it).background =
                            if (it <= idxOfClicked) resources.getDrawable(R.drawable.ic_star, null)
                            else resources.getDrawable(R.drawable.ic_star_empty, null)
                    }
                }
            }
        }
    }
}