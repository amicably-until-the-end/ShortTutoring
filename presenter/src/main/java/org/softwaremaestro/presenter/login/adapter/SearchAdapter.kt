package org.softwaremaestro.presenter.login.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import org.softwaremaestro.presenter.R


class SearchAdapter(
    context: Context,
    items: List<String>,
    private val onItemClick: (String) -> Unit
) :
    ArrayAdapter<String>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view =
            convertView ?: LayoutInflater.from(context).inflate(R.layout.item_search, parent, false)

        val tvName = view.findViewById<TextView>(R.id.tv_name)
        getItem(position)?.let { name ->
            tvName.text = name
            view.setOnClickListener { onItemClick(name) }
        }

        return view
    }
}