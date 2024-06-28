package com.group4.fruitopia.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.group4.fruitopia.MainActivity
import com.group4.fruitopia.data.service.ApiClient
import com.group4.fruitopia.data.service.ApiService
import com.group4.fruitopia.data.service.UserResponse
import com.group4.fruitopia.databinding.ActivityLoginBinding
import com.group4.fruitopia.register.RegisterActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }


    object SharedPrefConstants {
        const val SHARED_PREF_NAME = "login_shared_pref"
        // Key untuk menyimpan ID pengguna di SharedPreferences
        const val KEY_USER_ID = "user_id"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.tvRegisterHere.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.buttonSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 8) {
            Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
            return
        }

        val apiService = ApiClient.client?.create(ApiService::class.java)
        val call: Call<UserResponse>? = apiService?.loginUser(email, password)

        call?.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    if (userResponse?.result_code == 1) {

                        val sharedPreferences = getSharedPreferences(SharedPrefConstants.SHARED_PREF_NAME, MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        userResponse.user?.let { editor.putInt(SharedPrefConstants.KEY_USER_ID, it.id) } // Simpan ID pengguna
                        editor.apply()

                        Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                        navigateToMainActivity()
                    } else {
                        Toast.makeText(this@LoginActivity, "Cek kembali, Email atau Password salah", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("LoginActivity", "Failed to retrieve user: ${response.code()}")
                    Toast.makeText(this@LoginActivity, "Failed to retrieve user: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("LoginActivity", "Error logging in", t)
                Toast.makeText(this@LoginActivity, "Failed to login: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
