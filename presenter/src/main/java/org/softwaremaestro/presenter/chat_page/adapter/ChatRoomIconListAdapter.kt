package org.softwaremaestro.presenter.chat_page.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.ItemTutoringListRoomIconBinding
import org.softwaremaestro.presenter.util.Util

class ChatRoomIconListAdapter(
    private val onQuestionClick: (List<ChatRoomVO>, String, RecyclerView.Adapter<*>) -> Unit
) :
    RecyclerView.Adapter<ChatRoomIconListAdapter.ViewHolder>() {

    private var items: List<ChatRoomVO> = emptyList()

    private var selectedView: MaterialCardView? = null

    var selectedQuestionId: String? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = ItemTutoringListRoomIconBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(items[position], position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItem(items: List<ChatRoomVO>) {
        this.items = items
    }

    fun changeSelectedQuestionId(questionId: String?) {
        selectedQuestionId = questionId
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemTutoringListRoomIconBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: ChatRoomVO, position: Int) {
            binding.apply {
                root.setOnClickListener {
                    Log.d("ChatRoomIconListAdapter", "selectedPosition $selectedQuestionId")
                    if (item.questionId != selectedQuestionId) {
                        onQuestionClick(
                            item.teachers ?: emptyList(),
                            item.questionId ?: "",
                            this@ChatRoomIconListAdapter
                        )
                        selectedQuestionId = item.questionId ?: ""
                    }
                    Log.d("ChatRoomIconListAdapter", "all items $items")
                    cvContainer.strokeColor =
                        binding.root.context.getColor(R.color.primary_blue)
                    cvContainer.setBackgroundColor(binding.root.context.getColor(R.color.background_light_blue))
                    selectedView = cvContainer
                    cvContainer.strokeColor = binding.root.context.getColor(R.color.primary_blue)
                    cvContainer.setBackgroundColor(binding.root.context.getColor(R.color.background_light_blue))
                    selectedView = cvContainer
                }
                if (item.questionId == selectedQuestionId) {
                    cvContainer.strokeColor = binding.root.context.getColor(R.color.primary_blue)
                    cvContainer.setBackgroundColor(binding.root.context.getColor(R.color.background_light_blue))
                } else {
                    cvContainer.strokeColor = binding.root.context.getColor(R.color.background_grey)
                    cvContainer.setBackgroundColor(binding.root.context.getColor(R.color.transparent))
                }

                Glide.with(binding.root.context)
                    .load(item.roomImage)
                    .into(ivImage)

            }
        }
    }
}