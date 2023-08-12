package org.softwaremaestro.presenter.teacher_home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.softwaremaestro.domain.question_get.entity.QuestionGetResponseVO
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.ItemQuestionBinding

private const val EMPTY_STRING = "-"

class QuestionAdapter(private val onOfferBtnClickListener: (String, ViewHolder) -> Unit) :
    ListAdapter<QuestionGetResponseVO, QuestionAdapter.ViewHolder>(QuestionDiffUtil) {

    // 신청하기 버튼의 색을 결정하기 위해 선택된 질문 id를 저장한다
    var selectedQuestionId: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionAdapter.ViewHolder {
        val view = ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionAdapter.ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // 신청하기 버튼의 색을 결정하는 변수
        private var selected = false

        fun onBind(item: QuestionGetResponseVO) {

            // 기존에 선택된 질문 id와 나의 질문 id가 같으면 선택된 상태라고 판단
            selected = selectedQuestionId == item.id

            with(binding) {

                Picasso.with(root.context).load(item.problemImage).fit().centerCrop().into(ivPhoto)

                tvSubject.text = item.problemSchoolSubject ?: EMPTY_STRING
                tvDifficulty.text = item.problemDifficulty ?: EMPTY_STRING
                tvDesciption.text = item.problemDescription ?: EMPTY_STRING

                if (selected) {
                    binding.btnOffer.setBackgroundResource(R.drawable.btn_corner_radius_10_disabled)
                    binding.btnOffer.text = "신청 완료"
                } else {
                    binding.btnOffer.setBackgroundResource(R.drawable.btn_corner_radius_10_enabled)
                    binding.btnOffer.text = "신청하기"
                }

                btnOffer.setOnClickListener {
                    item.id?.let { onOfferBtnClickListener(it, this@ViewHolder) }
                }
            }
        }

        fun toggleSelectedAndChangeColor() {
            selected = !selected

            if (selected) {
                binding.btnOffer.setBackgroundResource(R.drawable.btn_corner_radius_10_disabled)
                binding.btnOffer.text = "신청 완료"
            } else {
                binding.btnOffer.setBackgroundResource(R.drawable.btn_corner_radius_10_enabled)
                binding.btnOffer.text = "신청하기"
            }
        }
    }
}

object QuestionDiffUtil : DiffUtil.ItemCallback<QuestionGetResponseVO>() {

    override fun areItemsTheSame(
        oldItem: QuestionGetResponseVO,
        newItem: QuestionGetResponseVO
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: QuestionGetResponseVO,
        newItem: QuestionGetResponseVO
    ): Boolean {
        return oldItem.id == newItem.id
    }
}