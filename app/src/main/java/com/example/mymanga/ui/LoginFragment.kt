package com.example.mymanga.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.mymanga.R
import com.example.mymanga.data.AuthViewModel
import com.example.mymanga.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // Inyectamos el ViewModel usando Hilt
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuración del botón de login
        binding.loginButton.setOnClickListener {
            val username = binding.usernameInput.editText?.text.toString().trim()
            val password = binding.passwordInput.editText?.text.toString().trim()


            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Llamada al ViewModel para realizar el login
                authViewModel.login(username, password)
            } else {
                Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Observamos los resultados del login
        authViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                // Login exitoso
                Toast.makeText(requireContext(), "Bienvenido: ${response.user.username}", Toast.LENGTH_SHORT).show()

                // Navegación al HomeFragment
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.loginFragment, true) // Eliminamos LoginFragment del back stack
                    .build()
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment, null, navOptions)
            }.onFailure { exception ->
                // Error en el login
                Toast.makeText(requireContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
            }
        }

        // Enlace para registrarse
        binding.registerLink.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
