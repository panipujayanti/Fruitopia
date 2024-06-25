package com.group4.fruitopia.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.group4.fruitopia.MainActivity
import com.group4.fruitopia.R
import com.group4.fruitopia.apiclient.ApiClient
import com.group4.fruitopia.apiservice.ApiService
import com.group4.fruitopia.databinding.ActivityRegisterBinding
import com.group4.fruitopia.login.LoginActivity
import com.group4.fruitopia.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tvLoginHere.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.buttonSignUp.setOnClickListener {
            addUser()
        }
    }

    private fun addUser() {
        val email = binding.etEmail.text.toString()
        val nama = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()

        if (email.isEmpty() || nama.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 8) {
            Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
            return
        }


        val apiService = ApiClient.getClient?.create(ApiService::class.java)
        val user = User(email = email, nama = nama, password = password)
        val call: Call<Void>? = apiService?.insertUser(user)

        call?.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "User added successfully", Toast.LENGTH_SHORT).show()
                    navigateToMainActivity()
                } else {
                    Toast.makeText(this@RegisterActivity, "Failed to add user", Toast.LENGTH_SHORT).show()
                }
            }


            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Failed to add user", Toast.LENGTH_SHORT).show()
                Log.e("RegisterActivity", "Error adding user", t)
            }
        })
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
