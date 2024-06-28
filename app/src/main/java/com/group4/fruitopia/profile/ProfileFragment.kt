package com.group4.fruitopia.profile

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.group4.fruitopia.R
import com.group4.fruitopia.data.model.User
import com.group4.fruitopia.data.service.ApiClient
import com.group4.fruitopia.data.service.ApiService
import com.group4.fruitopia.data.service.UpdateResponse
import com.group4.fruitopia.data.service.UserResponse
import com.group4.fruitopia.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    object SharedPrefConstants {
        const val SHARED_PREF_NAME = "login_shared_pref"
        const val KEY_USER_ID = "user_id"
    }

    private var userId: Int = 0
    private var email: String? = null
    private var nama: String? = null
    private var password: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences(SharedPrefConstants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        userId = sharedPreferences.getInt(SharedPrefConstants.KEY_USER_ID, -1)

        if (userId != -1) {
            fetchUserProfile(userId, view)
        } else {
            Log.e("ProfileFragment", "User ID not found in SharedPreferences")
        }

        setupLogout(view)
        setupUpdateProfile(view)

        return view
    }

    private fun fetchUserProfile(userId: Int, view: View) {
        val apiService = ApiClient.client?.create(ApiService::class.java)
        val call: Call<UserResponse> = apiService!!.profileUser(userId.toString())

        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                Log.d("ProfileFragment", "Response code: ${response.code()}")
                if (response.isSuccessful) {
                    val userProfile = response.body()?.user
                    if (userProfile != null) {
                        nama = userProfile.nama
                        email = userProfile.email
                        password = userProfile.password
                        Log.d("ProfileFragment", "User: ${userProfile.nama}, Email: ${userProfile.email}")
                        view.findViewById<TextView>(R.id.nama).text = nama
                        view.findViewById<TextView>(R.id.email).text = email
                    } else {
                        Log.e("ProfileFragment", "User profile is null")
                    }
                } else {
                    Log.e("ProfileFragment", "Failed to get user profile: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("ProfileFragment", "Error: ${t.message}")
            }
        })
    }

    private fun setupLogout(view: View) {
        view.findViewById<View>(R.id.ll_logout).setOnClickListener {
            // Clear SharedPreferences
            val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences(SharedPrefConstants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            // Start LoginActivity
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun setupUpdateProfile(view: View) {
        view.findViewById<Button>(R.id.btn_change_profile).setOnClickListener {
            showUpdateUserDialog(view)
        }
    }

    private fun showUpdateUserDialog(view: View?) {
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_update_profile, null)

        val editTextName = dialogView.findViewById<EditText>(R.id.editTextName)
        val editTextEmail = dialogView.findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = dialogView.findViewById<EditText>(R.id.editTextPassword)

        // Bind data profil ke EditText
        editTextName.setText(nama)
        editTextEmail.setText(email)
        editTextPassword.setText(password)

        builder.setView(dialogView)
        builder.setPositiveButton("Update") { dialog, which ->
            val name = editTextName.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                updateUser(userId, name, email, password)
                Toast.makeText(context, "Update " + userId.toString(), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.create().show()
    }

    private fun updateUser(userId: Int, name: String, email: String, password: String) {
        val apiService = ApiClient.client?.create(ApiService::class.java)
        val call: Call<UpdateResponse>? = apiService?.updateUserProfile(userId.toString(), email, name, password)

        call?.enqueue(object : Callback<UpdateResponse> {
            override fun onResponse(call: Call<UpdateResponse>, response: Response<UpdateResponse>) {
                if (response.isSuccessful) {
                    val updateResponse = response.body()
                    if (updateResponse?.status == "success") {
                        Toast.makeText(context, "Update profile successful", Toast.LENGTH_SHORT).show()
                        fetchUserProfile(userId, requireView())  // Refresh user profile
                    } else {
                        Toast.makeText(context, "Failed to update profile: ${updateResponse?.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("ProfileFragment", "Failed to update profile: ${response.code()}")
                    Toast.makeText(context, "Failed to update profile: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                Log.e("ProfileFragment", "Error updating user", t)
                Toast.makeText(context, "Error updating user: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
