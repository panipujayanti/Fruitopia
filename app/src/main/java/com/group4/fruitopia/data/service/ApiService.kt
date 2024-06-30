package com.group4.fruitopia.data.service

import com.group4.fruitopia.data.model.Order
import com.group4.fruitopia.data.model.Product
import com.group4.fruitopia.data.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    //product
    @GET("get_products.php")
    fun getProduct(): Call<List<Product>>

    @GET("user/{id}")
    fun getUser(@Path("id") userId: Int): Call<User>

    //user
    @POST("insert_user.php")
    fun insertUser(@Body user: User): Call<Void>


    @FormUrlEncoded
    @POST("update_profile.php")
    fun updateUserProfile(
        @Field("id") userId: String,
        @Field("email") email: String,
        @Field("nama") name: String,
        @Field("password") password: String
    ): Call<UpdateResponse>

    @FormUrlEncoded
    @POST("get_user_profile.php")
    fun profileUser(
        @Field("user_id") userId: String,
    ): Call<UserResponse>


    @FormUrlEncoded
    @POST("get_user_by_email.php")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<UserResponse>

    @POST("input_produk.php")
    fun inputOrder(@Body order: Order): Call<Void>
}

data class UserResponse(
    val status: String,
    val result_code: Int,
    val user: User?,
)

data class UpdateResponse(
     val status: String,
    val message: String
)

