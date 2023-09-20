package org.softwaremaestro.presenter.chat_page.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.util.Util
import org.softwaremaestro.presenter.chat_page.item.ChatRoom
import org.softwaremaestro.presenter.databinding.ItemTutoringListRoomIconBinding

class ChatRoomIconListAdapter(
    private val onQuestionClick: (String, Int, RecyclerView.Adapter<*>) -> Unit,
    private val onTeacherClick: (String, RecyclerView.Adapter<*>) -> Unit
) :
    RecyclerView.Adapter<ChatRoomIconListAdapter.ViewHolder>() {

    private var items: List<ChatRoomVO> = emptyList()

    private var selectedView: MaterialCardView? = null

    private var selectedPosition: Int = -1

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

    fun clearSelectedView(caller: RecyclerView.Adapter<*>?) {
        if (caller == null) {
            selectedView?.let {
                it.strokeColor = it.context.getColor(R.color.background_grey)
                selectedView = null
            }
        }
    }

    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemTutoringListRoomIconBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: ChatRoomVO, position: Int) {
            binding.apply {
                if (true) {
                    root.setOnClickListener {
                        onQuestionClick(
                            "1234", position,
                            this@ChatRoomIconListAdapter
                        )
                    }
                    cvImage.radius = Util.toPx(4, binding.root.context).toFloat()
                } else {
                    cvImage.radius = Util.toPx(4, binding.root.context).toFloat()
                    root.setOnClickListener {
                        onTeacherClick("234", this@ChatRoomIconListAdapter)
                        clearSelectedView(null)
                        cvContainer.strokeWidth = Util.toPx(1, binding.root.context)
                        cvContainer.strokeColor =
                            binding.root.context.getColor(R.color.primary_blue)
                        selectedView = cvContainer
                    }
                }
                if (position == selectedPosition) {
                    cvContainer.strokeColor = binding.root.context.getColor(R.color.primary_blue)
                    selectedView = cvContainer
                }
                Glide.with(binding.root.context)
                    .load(item.roomImage)
                    .into(ivImage)
            }
        }
    }
}