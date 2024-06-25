package com.group4.fruitopia.data.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id") val id: Int,
    @SerializedName("nama_buah") val namaBuah: String,
    @SerializedName("harga") val harga: String,
    @SerializedName("img") val img: String
)


