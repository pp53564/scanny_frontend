package com.scanny_project.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ui_ux_demo.databinding.ItemLanguageOptionBinding

class LanguageAdapter(
    private val items: List<LanguageOption>,
    private val onLanguageClicked: (LanguageOption) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val binding = ItemLanguageOptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LanguageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class LanguageViewHolder(private val binding: ItemLanguageOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LanguageOption) {
            binding.languageName.text = item.name
            binding.flagImage.setImageResource(item.flagResId)
            binding.root.setOnClickListener {
                onLanguageClicked(item)
            }
        }
    }
}