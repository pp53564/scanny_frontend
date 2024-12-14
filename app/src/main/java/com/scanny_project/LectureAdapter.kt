package com.scanny_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ui_ux_demo.R
import com.scanny_project.data.model.LectureDTO
import android.widget.TextView

class LecturesAdapter(
    private val lectures: List<LectureDTO>,
    private val onLectureClick: (LectureDTO) -> Unit
) : RecyclerView.Adapter<LecturesAdapter.LectureViewHolder>() {

    class LectureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lectureTitle: TextView = itemView.findViewById(R.id.lectureTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LectureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return LectureViewHolder(view)
    }

    override fun onBindViewHolder(holder: LectureViewHolder, position: Int) {
        val lecture = lectures[position]
        holder.lectureTitle.text = lecture.title
        holder.itemView.setOnClickListener {
            onLectureClick(lecture)
        }
    }

    override fun getItemCount() = lectures.size
}
