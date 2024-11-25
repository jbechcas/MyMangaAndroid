/*package com.example.mymanga.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mymanga.R
import com.example.mymanga.data.ApiClient
import com.example.mymanga.data.LoginRequest
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val usernameField: EditText = view.findViewById(R.id.username_input)
        val passwordField: EditText = view.findViewById(R.id.password_input)
        val loginButton: Button = view.findViewById(R.id.login_button)

        loginButton.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                performLogin(username, password)
            } else {
                showToast("No dejes las casillas vacías")
            }
        }

        return view
    }

    private fun performLogin(username: String, password: String) {
        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.login(LoginRequest(username, password))

                if (isAdded) {
                    showToast("Bienvenido ${response.user.username}!")
                    // Navegar al HomeFragment
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
            } catch (e: Exception) {
                if (isAdded) {
                    showToast("Login failed: ${e.message}")
                }
            }
        }
    }

    private fun showToast(message: String) {
        if (isAdded) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }
}*/

package com.example.mymanga.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mymanga.R

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val loginButton: Button = view.findViewById(R.id.login_button)

        loginButton.setOnClickListener {
            // Navegar directamente al HomeFragment sin lógica adicional
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }

        return view
    }
}

