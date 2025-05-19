package com.example.mymanga.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.mymanga.R
import com.example.mymanga.data.AuthViewModel
import com.example.mymanga.data.ProfileViewModel
import com.example.mymanga.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!authViewModel.isUserLoggedIn()) {
            navigateToLogin()
            return
        }

        setupObservers()
        setupButtons()
        displayUserInfo()
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun setupObservers() {
        profileViewModel.profileData.observe(viewLifecycleOwner) { person ->
            binding.progressBar.visibility = View.GONE

            if (person != null) {
                binding.nameInput.setText(person.name)
                binding.surnameInput.setText(person.surname)
                loadProfileImage(person.imageUrl)
            } else {
                binding.nameInput.setText("")
                binding.surnameInput.setText("")
                binding.profileImage.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        }

        profileViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        profileViewModel.operationResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                Toast.makeText(requireContext(), "Error: ${exception.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupButtons() {
        binding.saveButton.setOnClickListener {
            updateProfile()
        }

        binding.logoutButton.setOnClickListener {
            authViewModel.logout()
            navigateToLogin()
        }
    }

    private fun displayUserInfo() {
        val username = profileViewModel.getUserName() ?: "Usuario"
        val email = profileViewModel.getUserEmail() ?: "correo@ejemplo.com"

        binding.usernameText.text = username
        binding.emailText.text = email
    }

    private fun loadProfileImage(imageUrl: String?) {
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .circleCrop()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(binding.profileImage)
        } else {
            binding.profileImage.setImageResource(android.R.drawable.ic_menu_gallery)
        }
    }

    private fun updateProfile() {
        val name = binding.nameInput.text.toString().trim()
        val surname = binding.surnameInput.text.toString().trim()

        if (name.isEmpty() || surname.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        profileViewModel.updateProfile(name, surname)
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.action_item_4_to_loginFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}