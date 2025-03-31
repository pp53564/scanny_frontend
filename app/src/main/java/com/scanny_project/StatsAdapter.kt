package com.scanny_project

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ui_ux_demo.R
import com.scanny_project.data.model.StatsPerUserAndLanguageDTO

class StatsAdapter(
    private val items: List<StatsPerUserAndLanguageDTO>
) : RecyclerView.Adapter<StatsAdapter.StatsViewHolder>() {

    class StatsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val flagImage: ImageView = view.findViewById(R.id.flagImage)
        val langTitle: TextView = view.findViewById(R.id.langTitle)
        val langStats: TextView = view.findViewById(R.id.langStats)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_language_stat, parent, false)
        return StatsViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatsViewHolder, position: Int) {
        val stat = items[position]

        holder.langTitle.text = "Language: ${stat.language.uppercase()}"

        holder.langStats.text = """
            Accuracy: ${stat.accuracy}%
            Correct: ${stat.correct} / ${stat.answered}
            Total Questions: ${stat.totalQuestions}
            Avg Attempts: ${stat.avgAttemptsPerQuestion}
            Avg (Correct): ${stat.avgAttemptsForCorrect}
        """.trimIndent()

        val flagResId = getFlagResource(holder.itemView.context, stat.language)
        holder.flagImage.setImageResource(flagResId)
    }

    override fun getItemCount() = items.size

    private fun getFlagResource(context: Context, langCode: String): Int {
        return context.resources.getIdentifier(
            "flag_${langCode.lowercase()}",
            "drawable",
            context.packageName
        ).takeIf { it != 0 } ?: R.drawable.uk_flag
    }
}
