package org.softwaremaestro.presenter.util.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.WidgetStarRatingBarBinding
import org.softwaremaestro.presenter.util.Util.toPx

class StarRatingBar(private val context: Context, private val attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private val binding =
        WidgetStarRatingBarBinding.inflate(LayoutInflater.from(context), this, true)

    private var numOfStars = -1
    private var starSize = -1
    lateinit var onClick: (Int) -> Unit

    init {
        setAttrs()
        addStar()
        setStar()
    }

    private fun setAttrs() {
        attrs?.let {
            context.obtainStyledAttributes(it, R.styleable.StarRatingBar).apply {
                numOfStars = getInt(R.styleable.StarRatingBar_numOfStars, 5)
                starSize = getInt(R.styleable.StarRatingBar_starSize, 24)
                recycle()
            }
        }
    }

    private fun addStar() {
        if (numOfStars >= 0) {
            for (i in 0 until numOfStars) {
                binding.containerStar.addView(ImageView(context, attrs).apply {
                    layoutParams =
                        LinearLayout.LayoutParams(toPx(starSize, context), toPx(starSize, context))
                    background = resources.getDrawable(R.drawable.ic_star, null)
                    id = i
                })
            }
        }
    }

    private fun setStar() {
        binding.containerStar.children.forEach { child ->
            child.setOnClickListener { clicked ->
                getIdx(clicked)?.let { idx ->
                    fillStars(idx)
                    onClick(idx)
                }
            }
        }
    }

    private fun getIdx(clicked: View): Int? {
        return (0 until numOfStars).map { j ->
            Pair(j, binding.containerStar.getChildAt(j).id)
        }.find { child ->
            child.second == clicked.id
        }?.first
    }

    private fun fillStars(idx: Int) {
        for (i in 0 until numOfStars) {
            binding.containerStar.getChildAt(i).background =
                if (i <= idx) resources.getDrawable(R.drawable.ic_star, null)
                else resources.getDrawable(R.drawable.ic_star_empty, null)
        }
    }
}

