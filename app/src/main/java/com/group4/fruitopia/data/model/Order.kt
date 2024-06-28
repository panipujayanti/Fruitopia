package com.group4.fruitopia.data.model

data class Order(
    val id: Int = 0,  // Default value of 0 for id
    val nama_buah: String,
    val harga: String,
    val nama_penerima: String,
    val alamat: String,
    val tanggal: String
)
