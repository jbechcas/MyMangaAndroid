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

        binding.registerButton.setOnClickListener {
            val username = binding.usernameRegister.editText?.text.toString().trim()
            val email = binding.emailRegister.editText?.text.toString().trim()
            val name = binding.nameRegister.editText?.text.toString().trim()
            val surname = binding.surnameRegister.editText?.text.toString().trim()
            val password = binding.passwordRegister.editText?.text.toString().trim()
            val confirmPassword = binding.confirmPasswordRegister.editText?.text.toString().trim()

            if (validateInputs(username, email, name, surname, password, confirmPassword)) {
                authViewModel.register(username, email, password, name, surname)
            }
        }

        authViewModel.registerResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }.onFailure { exception ->
                Toast.makeText(requireContext(), "Error: ${exception.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validateInputs(
        username: String,
        email: String,
        name: String,
        surname: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        return when {
            username.isEmpty() -> {
                showToast("El nombre de usuario no puede estar vacío")
                false
            }
            email.isEmpty() -> {
                showToast("El correo electrónico no puede estar vacío")
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showToast("El formato del correo electrónico no es válido")
                false
            }
            name.isEmpty() -> {
                showToast("El nombre no puede estar vacío")
                false
            }
            surname.isEmpty() -> {
                showToast("El apellido no puede estar vacío")
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

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}