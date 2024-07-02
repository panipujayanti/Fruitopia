package com.group4.fruitopia.data.model

data class Order(
    val id: Int?,
    val nama_buah: String,
    val price_per_item: String,
    val harga: String,
    val nama_penerima: String,
    val alamat: String,
    val tanggal: String,
    val jumlah_barang: Int
)
