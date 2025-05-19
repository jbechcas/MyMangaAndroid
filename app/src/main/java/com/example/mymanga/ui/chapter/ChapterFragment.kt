package com.example.mymanga.ui.chapter

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mymanga.data.chapter.Chapter
import com.example.mymanga.data.chapter.ChapterViewModel
import com.example.mymanga.data.manga.Manga
import com.example.mymanga.databinding.DialogChapterBinding
import com.example.mymanga.databinding.FragmentChapterBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChapterFragment : Fragment() {

    private var _binding: FragmentChapterBinding? = null
    private val binding get() = _binding!!

    private val chapterViewModel: ChapterViewModel by viewModels()
    private lateinit var chapterAdapter: ChapterAdapter
    private var mangasList: List<Manga> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChapterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configurarRecyclerView()
        configurarObservadores()
        configurarBotones()
    }

    private fun configurarRecyclerView() {
        chapterAdapter = ChapterAdapter(
            onItemClick = { chapter ->
                Toast.makeText(requireContext(), "Capítulo: ${chapter.title}", Toast.LENGTH_SHORT).show()
            },
            onEditar = { chapter -> mostrarDialogoEdicion(chapter) },
            onEliminar = { chapter -> confirmarEliminacion(chapter) }
        )
        binding.recyclerView.adapter = chapterAdapter
    }

    private fun configurarObservadores() {
        chapterViewModel.chapters.observe(viewLifecycleOwner) { chapters ->
            chapterAdapter.submitList(chapters)
            binding.progressBar.visibility = View.GONE

            if (chapters.isEmpty()) {
                binding.emptyStateGroup.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.emptyStateGroup.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        }

        chapterViewModel.mangas.observe(viewLifecycleOwner) { mangas ->
            mangasList = mangas
        }

        chapterViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        chapterViewModel.operationResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                Toast.makeText(requireContext(), "Error: ${exception.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun configurarBotones() {
        binding.fabAddChapter.setOnClickListener {
            mostrarDialogoCreacion()
        }

        binding.retryButton.setOnClickListener {
            chapterViewModel.loadChapters()
        }
    }

    private fun mostrarDialogoCreacion() {
        val dialogBinding = DialogChapterBinding.inflate(layoutInflater)
        configurarSelectorMangas(dialogBinding, null)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Crear Capítulo")
            .setView(dialogBinding.root)
            .setPositiveButton("Guardar") { _, _ ->
                val title = dialogBinding.titleInput.text.toString().trim()
                val description = dialogBinding.descriptionInput.text.toString().trim()
                val selectedMangaId = obtenerMangaSeleccionadoId(dialogBinding)

                if (validarCampos(title, description, selectedMangaId)) {
                    chapterViewModel.createChapter(title, description, selectedMangaId)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoEdicion(chapter: Chapter) {
        val dialogBinding = DialogChapterBinding.inflate(layoutInflater)

        dialogBinding.titleInput.setText(chapter.title)
        dialogBinding.descriptionInput.setText(chapter.description)
        configurarSelectorMangas(dialogBinding, chapter.mangaId)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Editar Capítulo")
            .setView(dialogBinding.root)
            .setPositiveButton("Actualizar") { _, _ ->
                val title = dialogBinding.titleInput.text.toString().trim()
                val description = dialogBinding.descriptionInput.text.toString().trim()
                val selectedMangaId = obtenerMangaSeleccionadoId(dialogBinding)

                if (validarCampos(title, description, selectedMangaId)) {
                    chapterViewModel.updateChapter(chapter.id, title, description, selectedMangaId)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun configurarSelectorMangas(dialogBinding: DialogChapterBinding, mangaIdSeleccionado: Int?) {
        val mangaTitles = mangasList.map { it.title }
        val adapter =
            ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, mangaTitles)
        (dialogBinding.mangaSelector as? AutoCompleteTextView)?.setAdapter(adapter)

        // Si hay un manga seleccionado, lo establecemos como valor inicial
        if (mangaIdSeleccionado != null) {
            val selectedManga = mangasList.find { it.id == mangaIdSeleccionado }
            if (selectedManga != null) {
                dialogBinding.mangaSelector.setText(selectedManga.title, false)
            }
        }
    }

    private fun obtenerMangaSeleccionadoId(dialogBinding: DialogChapterBinding): Int {
        val mangaTitle = dialogBinding.mangaSelector.text.toString()
        return mangasList.find { it.title == mangaTitle }?.id ?: 0
    }

    private fun confirmarEliminacion(chapter: Chapter) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Eliminar Capítulo")
            .setMessage("¿Estás seguro de que quieres eliminar '${chapter.title}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                chapterViewModel.deleteChapter(chapter.id)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun validarCampos(title: String, description: String, mangaId: Int): Boolean {
        when {
            title.isEmpty() -> {
                Toast.makeText(requireContext(), "El título no puede estar vacío", Toast.LENGTH_SHORT).show()
                return false
            }
            description.isEmpty() -> {
                Toast.makeText(requireContext(), "La descripción no puede estar vacía", Toast.LENGTH_SHORT).show()
                return false
            }
            mangaId <= 0 -> {
                Toast.makeText(requireContext(), "Debe seleccionar un manga", Toast.LENGTH_SHORT).show()
                return false
            }
            else -> return true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}