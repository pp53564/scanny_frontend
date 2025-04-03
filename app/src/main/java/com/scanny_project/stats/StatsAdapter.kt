package com.scanny_project.stats

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
        val correctAnswers: TextView = view.findViewById(R.id.tvCorrectAnswers)
        val langStats: TextView = view.findViewById(R.id.tvRank)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_language_stat, parent, false)
        return StatsViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatsViewHolder, position: Int) {
        val stat = items[position]

//        holder.langTitle.text = "Language: ${stat.languageCode.uppercase()}"

        holder.langStats.text = "Rank: ${stat.rank} / ${stat.totalUsersInLang}".trimIndent()
        holder.correctAnswers.text = "Točni odgovori: ${stat.correctAnswers}".trimIndent()

        if(getFlagResource(holder.itemView.context, stat.languageCode) != null) {
            val flagResId = getFlagResource(holder.itemView.context, stat.languageCode)
            holder.flagImage.setImageResource(flagResId)
        }
    }

    override fun getItemCount() = items.size

    private fun getFlagResource(context: Context, langCode: String): Int {
        return context.resources.getIdentifier(
            "flag_${langCode.lowercase()}",
            "drawable",
            context.packageName
        )
    }
}
