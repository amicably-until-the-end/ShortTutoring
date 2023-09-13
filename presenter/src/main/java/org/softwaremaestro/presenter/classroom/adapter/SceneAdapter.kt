package org.softwaremaestro.presenter.classroom.adapter

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.herewhite.sdk.Room
import com.herewhite.sdk.domain.Promise
import com.herewhite.sdk.domain.SDKError
import org.softwaremaestro.presenter.databinding.ItemWhiteboardSceneBinding

class SceneAdapter(
    private val whiteBoardRoom: Room, private val onItemClickListener: (String) -> Unit,
) :
    RecyclerView.Adapter<SceneAdapter.ViewHolder>() {

    private var items: MutableList<Bitmap?> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SceneAdapter.ViewHolder {
        val view =
            ItemWhiteboardSceneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SceneAdapter.ViewHolder, position: Int) {
        holder.onBind(items[position], position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItem(items: MutableList<Bitmap?>) {
        this.items = items
    }

    fun getPreview() {
        val scenes = whiteBoardRoom.scenes
        items = MutableList(scenes.size) { null }

        scenes.forEachIndexed { idx, it ->
            Log.d("agora", "scenePath：" + "/" + it.name)
            whiteBoardRoom.getScenePreviewImage("/${it.name}", object : Promise<Bitmap> {
                override fun then(img: Bitmap?) {
                    if (items.size > idx) {
                        items[idx] = img
                        notifyDataSetChanged()
                    }
                }

                override fun catchEx(e: SDKError?) {
                    Log.d("agora", e.toString())
                }
            })
        }
    }

    inner class ViewHolder(private val binding: ItemWhiteboardSceneBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: Bitmap?, position: Int) {
            if (item != null) {
                binding.ivPreview.setImageBitmap(item)
                binding.tvIndex.text = (position + 1).toString()
            }
        }
    }
}