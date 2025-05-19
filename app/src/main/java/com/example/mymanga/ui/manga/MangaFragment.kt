package com.example.mymanga.ui.manga

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mymanga.data.manga.Manga
import com.example.mymanga.data.manga.MangaViewModel
import com.example.mymanga.databinding.DialogMangaBinding
import com.example.mymanga.databinding.FragmentMangaBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MangaFragment : Fragment() {

    private var _binding: FragmentMangaBinding? = null
    private val binding get() = _binding!!

    private val mangaViewModel: MangaViewModel by viewModels()
    private lateinit var mangaAdapter: MangaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMangaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configurarRecyclerView()
        configurarObservadores()
        configurarBotones()
    }

    private fun configurarRecyclerView() {
        mangaAdapter = MangaAdapter(
            onItemClick = { manga ->
                Toast.makeText(requireContext(), "Manga: ${manga.title}", Toast.LENGTH_SHORT).show()
            },
            onEditar = { manga -> mostrarDialogoEdicion(manga) },
            onEliminar = { manga -> confirmarEliminacion(manga) }
        )
        binding.recyclerView.adapter = mangaAdapter
    }

    private fun configurarObservadores() {
        mangaViewModel.mangas.observe(viewLifecycleOwner) { mangas ->
            mangaAdapter.submitList(mangas)
            binding.progressBar.visibility = View.GONE

            if (mangas.isEmpty()) {
                binding.emptyStateGroup.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.emptyStateGroup.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        }

        mangaViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        mangaViewModel.operationResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                Toast.makeText(requireContext(), "Error: ${exception.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun configurarBotones() {
        binding.fabAddManga.setOnClickListener {
            mostrarDialogoCreacion()
        }

        binding.retryButton.setOnClickListener {
            mangaViewModel.loadMangas()
        }
    }

    private fun mostrarDialogoCreacion() {
        val dialogBinding = DialogMangaBinding.inflate(layoutInflater)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Crear Manga")
            .setView(dialogBinding.root)
            .setPositiveButton("Guardar") { _, _ ->
                val title = dialogBinding.titleInput.text.toString().trim()
                val description = dialogBinding.descriptionInput.text.toString().trim()

                if (validarCampos(title, description)) {
                    mangaViewModel.createManga(title, description)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoEdicion(manga: Manga) {
        val dialogBinding = DialogMangaBinding.inflate(layoutInflater)

        dialogBinding.titleInput.setText(manga.title)
        dialogBinding.descriptionInput.setText(manga.description)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Editar Manga")
            .setView(dialogBinding.root)
            .setPositiveButton("Actualizar") { _, _ ->
                val title = dialogBinding.titleInput.text.toString().trim()
                val description = dialogBinding.descriptionInput.text.toString().trim()

                if (validarCampos(title, description)) {
                    mangaViewModel.updateManga(manga.id, title, description)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun confirmarEliminacion(manga: Manga) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Eliminar Manga")
            .setMessage("¿Estás seguro de que quieres eliminar '${manga.title}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                mangaViewModel.deleteManga(manga.id)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun validarCampos(title: String, description: String): Boolean {
        when {
            title.isEmpty() -> {
                Toast.makeText(requireContext(), "El título no puede estar vacío", Toast.LENGTH_SHORT).show()
                return false
            }
            description.isEmpty() -> {
                Toast.makeText(requireContext(), "La descripción no puede estar vacía", Toast.LENGTH_SHORT).show()
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