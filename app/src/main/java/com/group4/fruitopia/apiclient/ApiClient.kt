package com.group4.fruitopia.apiclient

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {
    //link base url disesuaikan dengan Alamat ip masing-masing,
    private const val BASE_URL = "http://192.168.249.54/serverweb/"
    private var retrofit: Retrofit? = null
    val getClient: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }

}