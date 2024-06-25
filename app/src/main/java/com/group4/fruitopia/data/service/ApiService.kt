package com.group4.fruitopia.data.service

import com.group4.fruitopia.data.model.Product
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("get_products.php")
    fun getProduct(): Call<List<Product>>
}