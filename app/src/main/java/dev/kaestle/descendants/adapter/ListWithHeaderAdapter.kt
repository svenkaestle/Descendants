package dev.kaestle.descendants.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.imageview.ShapeableImageView
import dev.kaestle.descendants.R
import dev.kaestle.descendants.model.Person
import java.time.format.DateTimeFormatter

class ListWithHeaderAdapter(private val personsWithHeaders: List<Any>) : RecyclerView.Adapter<ViewHolder>() {

    companion object {
        const val HEADER = 0
        const val PERSON = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(if (viewType == HEADER) R.layout.simple_string_item else R.layout.list_item_small, parent, false)
        return if (viewType == HEADER) HeaderViewHolder(view) else PersonViewHolder(view)
    }

    override fun getItemCount(): Int = personsWithHeaders.size

    override fun getItemViewType(position: Int): Int {
        return if (personsWithHeaders[position] is Person) PERSON else HEADER
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is PersonViewHolder) {
            val person: Person = personsWithHeaders[position] as Person

            holder.ivAvatar.setImageResource(R.drawable.default_avatar)
            holder.tvName.text = person.name
            holder.tvBirthday.text = person.birthday.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            holder.tvHeight.text = "${ person.height } cm"
        }
        else if (holder is HeaderViewHolder) {
            val header: String = personsWithHeaders[position] as String

            holder.tvType.text = header
        }
    }

    inner class PersonViewHolder(itemView: View) : ViewHolder(itemView) {
        val ivAvatar: ShapeableImageView = itemView.findViewById(R.id.siv_avatar)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvBirthday: TextView = itemView.findViewById(R.id.tv_birthday)
        val tvHeight: TextView = itemView.findViewById(R.id.tv_height)
    }

    inner class HeaderViewHolder(itemView: View) : ViewHolder(itemView) {
        val tvType: TextView = itemView.findViewById(R.id.tv_item)
    }
}