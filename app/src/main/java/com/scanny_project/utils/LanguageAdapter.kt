package com.scanny_project.utils

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ui_ux_demo.databinding.ItemLanguageOptionBinding
import com.scanny_project.LanguageOption

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
            Log.i("petra", item.name + item.flagResId)
            binding.languageName.text = item.name
            binding.flagImage.setImageResource(item.flagResId)

            // Round rectangle background or shape
            // If you want to style it in item_language_option.xml

            binding.root.setOnClickListener {
                onLanguageClicked(item)
            }
        }
    }
}