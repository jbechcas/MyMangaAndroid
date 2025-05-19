package com.example.mymanga.ui.chapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mymanga.data.chapter.Chapter
import com.example.mymanga.databinding.ItemChapterBinding

class ChapterAdapter(
    private val onItemClick: (Chapter) -> Unit,
    private val onEditar: (Chapter) -> Unit,
    private val onEliminar: (Chapter) -> Unit
) : ListAdapter<Chapter, ChapterAdapter.ChapterViewHolder>(ChapterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        val binding = ItemChapterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ChapterViewHolder(private val binding: ItemChapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(chapter: Chapter) {
            binding.textMangaTitle.text = buildString {
        append("Manga: ")
        append(chapter.mangaTitle)
    }
            binding.textChapterTitle.text = chapter.title
            binding.textDescription.text = chapter.description

            binding.root.setOnClickListener { onItemClick(chapter) }
            binding.buttonEdit.setOnClickListener { onEditar(chapter) }
            binding.buttonDelete.setOnClickListener { onEliminar(chapter) }
        }
    }

    class ChapterDiffCallback : DiffUtil.ItemCallback<Chapter>() {
        override fun areItemsTheSame(oldItem: Chapter, newItem: Chapter): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Chapter, newItem: Chapter): Boolean {
            return oldItem == newItem
        }
    }
}