package com.scanny_project.features.teacher.lecture

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ui_ux_demo.databinding.ItemScannedBinding
import com.google.mlkit.nl.translate.Translator
import com.scanny_project.data.model.ScannedItem
import com.scanny_project.utils.LanguageData
import com.scanny_project.utils.TranslatorHelper

class ScannedItemsAdapter(
    private val onEditClick: (ScannedItem, Int) -> Unit
) : RecyclerView.Adapter<ScannedItemsAdapter.ViewHolder>() {

    private val items: MutableList<ScannedItem> = mutableListOf()

    inner class ViewHolder(private val binding: ItemScannedBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ScannedItem) {
            binding.tvItem.text = item.translations["hr"] ?: item.translations["en"] ?: item.name
            binding.cbSelect.isChecked = item.isChecked

            binding.cbSelect.setOnCheckedChangeListener { _, isChecked ->
                item.isChecked = isChecked
            }

            binding.edit.setOnClickListener {
                onEditClick(item, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemScannedBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newItems: List<ScannedItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun updateItem(idx: Int, item: ScannedItem) {
        items[idx] = item
        notifyItemChanged(idx)
    }

    fun findPosition(item: ScannedItem): Int = items.indexOf(item)

    fun getSelectedItems(): List<ScannedItem> =
        items.filter { it.isChecked }.map { it }
}
