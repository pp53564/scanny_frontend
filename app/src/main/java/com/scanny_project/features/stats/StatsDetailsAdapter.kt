package com.scanny_project.features.stats

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ui_ux_demo.R
import com.google.android.material.card.MaterialCardView
import com.scanny_project.data.model.NeighborDTO

class StatsDetailsAdapter(
    private var items: List<NeighborDTO>,
    private val currentUserName: String
) : RecyclerView.Adapter<StatsDetailsAdapter.StatsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_stats_details, parent, false)
        return StatsViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatsViewHolder, position: Int) {
        val currentItem = items[position]
        val isCurrentUser = currentItem.username == currentUserName
        holder.bind(currentItem, isCurrentUser)
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<NeighborDTO>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class StatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvUser: TextView = itemView.findViewById(R.id.tvUser)
        private val tvScore: TextView = itemView.findViewById(R.id.tvScore)
        private val cardView: MaterialCardView = itemView.findViewById(R.id.cardNeighborsStats)
        val context: Context = itemView.context

        fun bind(item: NeighborDTO, isCurrentUser: Boolean) {
              tvUser.text = item.username
              tvScore.text = item.score.toInt().toString()

            if (isCurrentUser) {
                tvUser.setTypeface(null, Typeface.BOLD)
                cardView.strokeColor = context.getColor(R.color.light_green)
            }
        }
    }
}
