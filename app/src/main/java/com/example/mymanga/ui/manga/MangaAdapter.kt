package com.example.mymanga.ui.manga

import android.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymanga.data.manga.Manga
import com.example.mymanga.databinding.ItemMangaBinding

class MangaAdapter(
    private val onItemClick: (Manga) -> Unit,
    private val onEditar: (Manga) -> Unit,
    private val onEliminar: (Manga) -> Unit
) : ListAdapter<Manga, MangaAdapter.MangaViewHolder>(MangaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MangaViewHolder {
        val binding = ItemMangaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MangaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MangaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MangaViewHolder(private val binding: ItemMangaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(manga: Manga) {
            binding.textTitle.text = manga.title
            binding.textDescription.text = manga.description

            if (manga.imageUrl != null) {
                Glide.with(binding.root.context)
                    .load(manga.imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_menu_gallery)
                    .error(R.drawable.ic_menu_report_image)
                    .into(binding.imageManga)
            } else {
                binding.imageManga.setImageResource(R.drawable.ic_menu_gallery)
            }

            binding.root.setOnClickListener { onItemClick(manga) }
            binding.buttonEdit.setOnClickListener { onEditar(manga) }
            binding.buttonDelete.setOnClickListener { onEliminar(manga) }
        }
    }

    class MangaDiffCallback : DiffUtil.ItemCallback<Manga>() {
        override fun areItemsTheSame(oldItem: Manga, newItem: Manga): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Manga, newItem: Manga): Boolean {
            return oldItem == newItem
        }
    }
}