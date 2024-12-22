package com.scanny_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ui_ux_demo.R
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import com.scanny_project.data.model.UserLectureDTO

class LecturesAdapter(
    private val lectures: List<UserLectureDTO>,
    private val onLectureClick: (UserLectureDTO) -> Unit
) : RecyclerView.Adapter<LecturesAdapter.LectureViewHolder>() {

    class LectureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lectureTitle: TextView = itemView.findViewById(R.id.lectureTitle)
        val lectureCard: MaterialCardView = itemView.findViewById(R.id.lecture_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LectureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return LectureViewHolder(view)
    }

    override fun onBindViewHolder(holder: LectureViewHolder, position: Int) {
        val lecture = lectures[position]
        holder.lectureTitle.text = lecture.title
        val context = holder.itemView.context
        if (lecture.allQuestionsSucceeded) {
            holder.lectureCard.strokeColor = context.getColor(R.color.light_green)
        } else {
            holder.lectureCard.strokeColor = context.getColor(R.color.light_red)
        }
        holder.itemView.setOnClickListener {
            onLectureClick(lecture)
        }
    }

    override fun getItemCount() = lectures.size
}
