package com.scanny_project.features.questions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.example.ui_ux_demo.R
import com.scanny_project.data.model.UserQuestionDTO

class QuestionAdapter(
    private val questions: List<UserQuestionDTO>,
    private val onQuestionClick: (UserQuestionDTO) -> Unit
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

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

//        holder.questionSubject.text = "Predmet ${position + 1}"
//        holder.questionAttempts.text = "Broj pokušaja: ${question.attemptCount}"
        val context = holder.itemView.context
        holder.questionSubject.text = context.getString(R.string.question_subject, position + 1)
        holder.questionAttempts.text = context.getString(R.string.question_attempts, question.attemptCount)

        if (question.succeeded) {
            holder.questionCard.strokeColor = context.getColor(R.color.light_green)
        } else {
            holder.questionCard.strokeColor = context.getColor(R.color.blue_gray)
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
