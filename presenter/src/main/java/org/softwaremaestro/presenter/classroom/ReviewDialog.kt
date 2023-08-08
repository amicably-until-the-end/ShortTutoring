package org.softwaremaestro.presenter.classroom

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.RatingBar
import android.widget.TextView
import org.softwaremaestro.presenter.R

class ReviewDialog(private val context: Context) : Dialog(context) {

    private lateinit var rbReview: RatingBar
    private lateinit var tvNoti: TextView
    private lateinit var tvCancel: TextView
    private lateinit var tvReview: TextView

    init {
        // 대화상자 둥글게 만들기
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.requestFeature(Window.FEATURE_NO_TITLE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_review)

        rbReview = findViewById(R.id.rb_review)
        tvNoti = findViewById(R.id.tv_noti)
        tvCancel = findViewById(R.id.tv_cancel)
        tvReview = findViewById(R.id.tv_review)


        rbReview.setOnRatingBarChangeListener { _, rating, _ ->
            val idx = rating.toInt() - 1
            tvNoti.text = context.resources.getStringArray(R.array.rating_noti)[idx]
        }

        tvCancel.setOnClickListener {
            dismiss()
        }

        tvReview.setOnClickListener {
            dismiss()
        }
    }
}