package com.example.mymanga

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mymanga.ui.LoginFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, LoginFragment())
            .commit()

    }
}