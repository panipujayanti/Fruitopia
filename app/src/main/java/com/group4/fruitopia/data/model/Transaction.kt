package com.group4.fruitopia.data.model

data class Transaction(
    val id: Int,
    val nama_buah: String,
    val product_image: String,
    val harga: String,
    val price_per_item: String,
    val jumlah_barang: Int,
    val nama_penerima: String,
    val alamat: String,
    val tanggal: String
)

