package com.group4.fruitopia.apiservice

import com.group4.fruitopia.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @POST("insert_user.php")
    fun insertUser(@Body user: User): Call<Void>

    @POST("get_users.php")
    fun getUsers(): Call<List<User>>

    @FormUrlEncoded
    @POST("get_user_by_email.php")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<UserResponse>
}

data class UserResponse(
    val status: String,
    val result_code: Int,
    val user: User?
)

