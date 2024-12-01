package com.example.mymanga.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mymanga.R
import com.example.mymanga.data.AuthViewModel
import com.example.mymanga.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    // Inyectar el ViewModel
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar el botón de registro
        binding.registerButton.setOnClickListener {
            val username = binding.usernameRegister.text.toString().trim()
            val email = binding.emailRegister.text.toString().trim()
            val password = binding.passwordRegister.text.toString().trim()
            val confirmPassword = binding.confirmPasswordRegister.text.toString().trim()

            if (validateInputs(username, email, password, confirmPassword)) {
                authViewModel.register(username, email, password)
            }
        }

        // Observar el resultado del registro
        authViewModel.registerResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }.onFailure { exception ->
                handleRegisterError(exception)
            }
        }
    }

    private fun validateInputs(username: String, email: String, password: String, confirmPassword: String): Boolean {
        return when {
            username.isEmpty() -> {
                showToast("El nombre de usuario no puede estar vacío")
                false
            }
            email.isEmpty() -> {
                showToast("El correo electrónico no puede estar vacío")
                false
            }
            password.isEmpty() -> {
                showToast("La contraseña no puede estar vacía")
                false
            }
            password.length < 6 -> {
                showToast("La contraseña debe tener al menos 6 caracteres")
                false
            }
            confirmPassword != password -> {
                showToast("Las contraseñas no coinciden")
                false
            }
            else -> true
        }
    }

    private fun handleRegisterError(exception: Throwable) {
        val errorMessage = exception.message ?: "Error desconocido"
        Toast.makeText(requireContext(), "Error en el registro: $errorMessage", Toast.LENGTH_LONG).show()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
