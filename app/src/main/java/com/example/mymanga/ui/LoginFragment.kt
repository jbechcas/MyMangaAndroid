package com.example.mymanga.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.mymanga.R

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_login, container, false)
        val btnLogin = root.findViewById<Button>(R.id.login_button)
        val btnRegister = root.findViewById<TextView>(R.id.register_link)

        btnLogin.setOnClickListener {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.loginFragment, true) // Esto hace que loginFragment sea eliminado del back stack
                .build()

            findNavController().navigate(R.id.action_loginFragment_to_homeFragment, null, navOptions)
        }

        btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }


        return root
    }



}