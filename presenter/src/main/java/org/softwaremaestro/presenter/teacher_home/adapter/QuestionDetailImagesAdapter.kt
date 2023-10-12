package org.softwaremaestro.presenter.teacher_home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.softwaremaestro.presenter.databinding.ItemQuestionDetailImageBinding

class QuestionDetailImagesAdapter :
    RecyclerView.Adapter<QuestionDetailImagesAdapter.ViewHolder>() {

    private var items: List<String> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuestionDetailImagesAdapter.ViewHolder {
        val view =
            ItemQuestionDetailImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionDetailImagesAdapter.ViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItem(items: List<String>) {
        Log.d("mymymy", items.toString())
        this.items = items
    }

    inner class ViewHolder(private val binding: ItemQuestionDetailImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: String?) {
            Glide.with(binding.root).load(item).centerCrop().into(binding.ivImage)
        }
    }

}