package com.scanny_project.features.stats

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ui_ux_demo.R
import com.scanny_project.data.model.StatsPerUserAndLanguageDTO
import com.scanny_project.utils.LanguageData

class StatsAdapter(
    private val items: List<StatsPerUserAndLanguageDTO>
) : RecyclerView.Adapter<StatsAdapter.StatsViewHolder>() {

    class StatsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val flagImage: ImageView = view.findViewById(R.id.flagImage)
        val langStats: TextView = view.findViewById(R.id.tvRank)
        val statsCardView: CardView = view.findViewById(R.id.cardLanguageStats)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_language_stat, parent, false)
        return StatsViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatsViewHolder, position: Int) {
        val stat = items[position]

        holder.langStats.text = "Rank: ${stat.rank} / ${stat.totalUsersInLang}".trimIndent()
        val flag = LanguageData.languages.firstOrNull { it.code == stat.languageCode }?.flagResId
            ?: R.drawable.fancy_card_background_blue
        holder.flagImage.setImageResource(flag)

        holder.statsCardView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, StatsDetailsActivity::class.java)
            intent.putExtra("LANGUAGE_CODE", stat.languageCode)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = items.size

}
