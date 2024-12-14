package com.scanny_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ui_ux_demo.R
import com.scanny_project.data.model.QuestionDTO
import android.widget.TextView

class QuestionsAdapter(
    private val questions: List<QuestionDTO>,
    private val onQuestionClick: (QuestionDTO) -> Unit
) : RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder>() {

    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionSubject: TextView = itemView.findViewById(R.id.questionSubject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.question_list_item, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        holder.questionSubject.text = question.subject
        holder.itemView.setOnClickListener {
            onQuestionClick(question)
        }
    }

    override fun getItemCount() = questions.size
}
