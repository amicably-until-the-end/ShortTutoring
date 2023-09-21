package org.softwaremaestro.presenter.chat_page.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.chat_page.item.ChatMsg
import org.softwaremaestro.presenter.databinding.ItemChatButtonsBinding
import org.softwaremaestro.presenter.databinding.ItemChatQuestionBinding
import org.softwaremaestro.presenter.databinding.ItemChatTextBinding

class MessageListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<ChatMsg> = emptyList()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_BUTTONS -> {
                val view = ItemChatButtonsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ButtonsViewHolder(view)
            }

            TYPE_QUESTION -> {
                val view = ItemChatQuestionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                QuestionViewHolder(view)
            }

            else -> {
                val view = ItemChatTextBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                TextViewHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        when (items[position].type) {
            TYPE_BUTTONS -> {
                (holder as ButtonsViewHolder).onBind(items[position])
            }

            TYPE_QUESTION -> {
                (holder as QuestionViewHolder).onBind(items[position])
            }

            else -> {
                (holder as TextViewHolder).onBind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItem(items: List<ChatMsg>) {
        this.items = items
    }

    inner class TextViewHolder(private val binding: ItemChatTextBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: ChatMsg) {
            binding.apply {
                if (!item.myMsg) {
                    //set colors
                    containerBody.backgroundTintList = root.context.getColorStateList(R.color.white)
                    tvText.setTextColor(root.context.getColor(R.color.black))
                    //set position to left
                    containerBody.updateLayoutParams<ConstraintLayout.LayoutParams> {
                        leftToLeft = root.id
                        rightToRight = ConstraintLayout.LayoutParams.UNSET
                    }
                    tvTime.updateLayoutParams<ConstraintLayout.LayoutParams> {
                        leftToRight = containerBody.id
                        rightToLeft = ConstraintLayout.LayoutParams.UNSET
                    }
                }
                tvText.text = item.message
                tvTime.text = item.time
            }
        }
    }

    inner class ButtonsViewHolder(private val binding: ItemChatButtonsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: ChatMsg) {
            binding.apply {
                if (!item.myMsg) {
                    //set colors
                    containerBody.backgroundTintList = root.context.getColorStateList(R.color.white)
                    tvText.setTextColor(root.context.getColor(R.color.black))
                    //set position to left
                    containerBody.updateLayoutParams<ConstraintLayout.LayoutParams> {
                        leftToLeft = root.id
                        rightToRight = ConstraintLayout.LayoutParams.UNSET
                    }
                    tvTime.updateLayoutParams<ConstraintLayout.LayoutParams> {
                        leftToRight = containerBody.id
                        rightToLeft = ConstraintLayout.LayoutParams.UNSET
                    }
                }
                tvText.text = item.message
                tvTime.text = item.time

                val buttons = listOf<Button>(
                    btn1, btn2, btn3
                )

                for (i in 0 until item.buttonNumber) {
                    buttons[i].visibility = Button.VISIBLE
                    buttons[i].text = item.buttonString?.get(i)
                    buttons[i].setOnClickListener {
                        //TODO: 리스너 구현
                    }
                }
            }
        }
    }

    inner class QuestionViewHolder(private val binding: ItemChatQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: ChatMsg) {
            binding.apply {
                if (!item.myMsg) {
                    //set position to left
                    containerBody.updateLayoutParams<ConstraintLayout.LayoutParams> {
                        leftToLeft = root.id
                        rightToRight = ConstraintLayout.LayoutParams.UNSET
                    }
                }
                tvDesciption.text = item.questionDesc
                Glide.with(root.context).load(item.questionImageUrl)
                    .centerCrop()
                    .into(ivImage)
            }
        }
    }

    companion object {
        const val TYPE_TEXT = 0
        const val TYPE_BUTTONS = 1
        const val TYPE_QUESTION = 2
    }
}