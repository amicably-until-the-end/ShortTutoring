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
import org.softwaremaestro.presenter.util.Util
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val EMPTY_STRING = "-"

class TeacherQuestionAdapter(
    private val onItemClickListener: (QuestionGetResponseVO) -> Unit,
) : ListAdapter<QuestionGetResponseVO, TeacherQuestionAdapter.ViewHolder>(QuestionDiffUtil) {

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
                tvSchoolLevelAndSubject.text =
                    if (item.problemSchoolLevel != null && item.problemSubject != null) {
                        "${item.problemSchoolLevel} ${item.problemSubject}"
                    } else EMPTY_STRING
                // 이미 신청한 수업인 경우
                if (SocketManager.userId != null && item.offerTeachers != null &&
                    SocketManager.userId!! in item.offerTeachers!!
                ) {
                    when (item.status) {
                        "pending" -> {
                            tvTime.visibility = View.GONE
                            ivCheck.visibility = View.VISIBLE
                            tvTimeText.text = "신청 완료"
                        }

                        "reserved" -> {
                            containerTime.visibility = View.VISIBLE
                            tvTime.visibility = View.VISIBLE
                            item.reservedStart?.let { setTimeText(it) }
                        }
                    }
                }
                // 신청하지 않은 수업인 경우
                else {
                    tvTime.visibility = View.VISIBLE
                    ivCheck.visibility = View.GONE
                    val times = "${
                        item.hopeTutoringTime?.map {
                            "${it.hour}:${
                                String.format(
                                    "%02d",
                                    it.minute
                                )
                            }"
                        }
                            ?.joinToString(", ") ?: ""
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

        private fun setTimeText(reservedStart: String) {
            val ldt = Util.toLocalDateTime(reservedStart)
            val date = if (ldt.dayOfMonth == LocalDateTime.now().dayOfMonth) "오늘"
            else if (ldt.dayOfMonth == LocalDateTime.now().plusDays(1L).dayOfMonth) "내일"
            else "${ldt.monthValue}. ${ldt.dayOfMonth}"
            val time = ldt.format(DateTimeFormatter.ofPattern("hh:mm"))
            binding.tvTimeText.text = "$date $time"
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
}