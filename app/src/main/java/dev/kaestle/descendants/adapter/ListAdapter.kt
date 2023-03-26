package dev.kaestle.descendants.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import dev.kaestle.descendants.R
import dev.kaestle.descendants.model.Person
import java.time.format.DateTimeFormatter

class ListAdapter(private val persons: List<Person>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    var onItemClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = persons.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person: Person = persons[position]

        if (person.imageUri != null) {
            holder.sivAvatar.setImageURI(person.imageUri)
        } else {
            holder.sivAvatar.setImageResource(R.drawable.default_avatar)
        }
        holder.tvName.text = person.name
        holder.tvBirthday.text = person.birthday.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        holder.tvHeight.text = "${ person.height } cm"
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sivAvatar: ShapeableImageView = itemView.findViewById(R.id.siv_avatar)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvBirthday: TextView = itemView.findViewById(R.id.tv_birthday)
        val tvHeight: TextView = itemView.findViewById(R.id.tv_height)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }
        }
    }
}