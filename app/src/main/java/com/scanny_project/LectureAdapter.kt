package com.scanny_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.ui_ux_demo.R
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import com.scanny_project.data.model.UserLectureDTO

class LecturesAdapter(
    private val lectures: List<UserLectureDTO>,
    private val languageCode: String,
    private val onLectureClick: (UserLectureDTO) -> Unit
) : RecyclerView.Adapter<LecturesAdapter.LectureViewHolder>() {

    class LectureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lectureTitle: TextView = itemView.findViewById(R.id.lectureTitle)
        val lectureCard: MaterialCardView = itemView.findViewById(R.id.lecture_item)
//        val flagImage: ImageView = itemView.findViewById(R.id.flagIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LectureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return LectureViewHolder(view)
    }

    override fun onBindViewHolder(holder: LectureViewHolder, position: Int) {
        val lecture = lectures[position]
        holder.lectureTitle.text = lecture.title
//        when (languageCode) {
//            "fr" -> {
//                holder.flagImage.setImageResource(R.drawable.france_flag)
////                holder.flagImage.flagIcon.visibility = View.VISIBLE
//            }
//        }
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
