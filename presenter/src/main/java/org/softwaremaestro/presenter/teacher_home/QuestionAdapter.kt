package org.softwaremaestro.presenter.teacher_home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.softwaremaestro.domain.question_get.entity.QuestionGetResponseVO
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.ItemQuestionBinding

private const val EMPTY_STRING = "-"

class QuestionAdapter(
    private val onImageClickListener: (QuestionGetResponseVO) -> Unit,
    private val onOfferBtnClickListener: (String, String) -> Unit
) : ListAdapter<QuestionGetResponseVO, QuestionAdapter.ViewHolder>(QuestionDiffUtil) {

    // 신청하기 버튼의 색을 결정하기 위해 선택된 질문 id를 저장한다
    var selectedQuestionId: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionAdapter.ViewHolder {
        val view = ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.hashCode().toLong()
    }

    inner class ViewHolder(private val binding: ItemQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // 뷰홀더가 처음 생성될 때만 실행되는 코드
        fun bind(item: QuestionGetResponseVO) {

            with(binding) {

                Glide.with(root.context).load(item.problemImage).centerCrop()
                    .into(ivPhoto)

                ivPhoto.setOnClickListener {
                    onImageClickListener(item)
                }

                tvSubjectAndDifficulty.text =
                    if (item.problemSchoolSubject != null && item.problemDifficulty != null) {
                        "${item.problemSchoolSubject} · ${item.problemDifficulty}"
                    } else EMPTY_STRING

                tvDesciption.text = item.problemDescription ?: EMPTY_STRING

                if (selectedQuestionId == item.id) {
                    binding.btnOffer.setBackgroundResource(R.drawable.bg_radius_10_dark_grey)
                    binding.btnOffer.text = "신청 완료"
                } else {
                    binding.btnOffer.setBackgroundResource(R.drawable.bg_radius_10_blue)
                    binding.btnOffer.text = "신청하기"
                }

                btnOffer.setOnClickListener {
                    if (item.id != null && item.problemImage != null) {
                        onOfferBtnClickListener(item.id!!, item.problemImage!!)
                    }
                }
            }
        }

        fun setActiveOnOfferButton(active: Boolean) {
            // 기존에 선택된 질문 id와 나의 질문 id가 같으면 선택된 상태라고 판단
            if (active) {
                binding.btnOffer.setBackgroundResource(R.drawable.bg_radius_10_dark_grey)
                binding.btnOffer.text = "신청 완료"
            } else {
                binding.btnOffer.setBackgroundResource(R.drawable.bg_radius_10_blue)
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