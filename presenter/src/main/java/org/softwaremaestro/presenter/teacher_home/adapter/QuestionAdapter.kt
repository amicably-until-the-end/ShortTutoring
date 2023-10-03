package org.softwaremaestro.presenter.teacher_home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.softwaremaestro.domain.question_get.entity.QuestionGetResponseVO
import org.softwaremaestro.domain.socket.SocketManager
import org.softwaremaestro.presenter.databinding.ItemQuestionBinding

private const val EMPTY_STRING = "-"

class QuestionAdapter(
    private val onItemClickListener: (QuestionGetResponseVO) -> Unit,
) : ListAdapter<QuestionGetResponseVO, QuestionAdapter.ViewHolder>(QuestionDiffUtil) {

    // 신청하기 버튼의 색을 결정하기 위해 선택된 질문 id를 저장한다
    var selectedQuestionId: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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

                Glide.with(root.context).load(item.mainImage)
                    .centerCrop()
                    .into(ivPhoto)

                tvDesciption.text = item.problemDescription ?: EMPTY_STRING

                tvSubject.text = item.problemSubject ?: EMPTY_STRING

                // 이미 신청한 수업인 경우
                if (SocketManager.userId != null && item.offerTeachers != null &&
                    SocketManager.userId!! in item.offerTeachers!!
                ) {
                    tvTime.visibility = View.GONE
                    ivCheck.visibility = View.VISIBLE
                    tvTimeText.text = "신청 완료"
                }
                // 신청하지 않은 수업인 경우
                else {
                    tvTime.visibility = View.VISIBLE
                    ivCheck.visibility = View.GONE
                    val times = "${if (item.hopeImmediately == true) "지금 바로, " else ""} ${
                        item.hopeTutoringTime?.joinToString(", ") ?: "undefined"
                    }"
                    tvTimeText.text = times
                }

                root.setOnClickListener {
                    if (item.id != null && item.images != null) {
                        onItemClickListener(item)
                    }
                }
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