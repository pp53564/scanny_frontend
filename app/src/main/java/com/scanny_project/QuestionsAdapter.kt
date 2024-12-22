package com.scanny_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.example.ui_ux_demo.R
import com.scanny_project.data.model.UserQuestionDTO

class QuestionsAdapter(
    private val questions: List<UserQuestionDTO>,
    private val onQuestionClick: (UserQuestionDTO) -> Unit
) : RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder>() {

    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionSubject: TextView = itemView.findViewById(R.id.questionSubject)
        val questionAttempts: TextView = itemView.findViewById(R.id.attemptsCount)
        val questionCard: MaterialCardView = itemView.findViewById(R.id.questionCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.question_list_item, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]

//        holder.questionSubject.text = question.subject
        holder.questionAttempts.text = "Broj pokušaja: ${question.attemptCount}"

        val context = holder.itemView.context
        if (question.succeeded) {
            holder.questionCard.strokeColor = context.getColor(R.color.light_green)
        } else {
            holder.questionCard.strokeColor = context.getColor(R.color.light_red)
        }

        holder.itemView.setOnClickListener {
            if(!question.succeeded) {
                onQuestionClick(question)
            } else {
                Toast.makeText(context, "Uspješno odgovoreno!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun getItemCount() = questions.size
}
