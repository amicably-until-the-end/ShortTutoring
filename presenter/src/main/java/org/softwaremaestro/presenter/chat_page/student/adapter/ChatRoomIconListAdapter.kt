package org.softwaremaestro.presenter.chat_page.student.adapter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.Util.Util
import org.softwaremaestro.presenter.chat_page.item.ChatRoom
import org.softwaremaestro.presenter.databinding.ItemTutoringListRoomIconBinding
import org.softwaremaestro.presenter.login.LoginActivity_GeneratedInjector

class ChatRoomIconListAdapter(
    private val onQuestionClick: (String, Int, RecyclerView.Adapter<*>) -> Unit,
    private val onTeacherClick: (String, RecyclerView.Adapter<*>) -> Unit
) :
    RecyclerView.Adapter<ChatRoomIconListAdapter.ViewHolder>() {

    private var items: List<ChatRoom> = emptyList()

    private var selectedView: MaterialCardView? = null

    private var selectedPosition: Int = -1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatRoomIconListAdapter.ViewHolder {
        val view = ItemTutoringListRoomIconBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatRoomIconListAdapter.ViewHolder, position: Int) {
        holder.onBind(items[position], position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItem(items: List<ChatRoom>) {
        this.items = items
    }

    fun clearSelectedView(caller: RecyclerView.Adapter<*>?) {
        if (caller == null) {
            selectedView?.strokeColor = selectedView?.context?.getColor(R.color.background_grey)!!
            selectedView = null
        }
    }

    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemTutoringListRoomIconBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: ChatRoom, position: Int) {
            binding.apply {
                if (item.roomType == 3) {
                    root.setOnClickListener {
                        onQuestionClick(
                            item.contentId, position,
                            this@ChatRoomIconListAdapter
                        )
                    }
                    cvImage.radius = Util.toPx(4, binding.root.context).toFloat()
                } else {
                    cvImage.radius = Util.toPx(4, binding.root.context).toFloat()
                    root.setOnClickListener {
                        onTeacherClick(item.contentId, this@ChatRoomIconListAdapter)
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
                    .load(item.imageUrl)
                    .into(ivImage)
            }
        }
    }
}