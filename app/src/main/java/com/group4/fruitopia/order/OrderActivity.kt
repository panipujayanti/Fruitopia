package com.group4.fruitopia.order

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.group4.fruitopia.data.model.Order
import com.group4.fruitopia.data.service.ApiClient
import com.group4.fruitopia.data.service.ApiService
import com.group4.fruitopia.databinding.ActivityOrderBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.group4.fruitopia.R
import com.group4.fruitopia.utils.toCurrencyFormat

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding
    private var productQuantity: Int = 1
    private var pricePerItem: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ivBack: ImageView = findViewById(R.id.iv_back)
        ivBack.setOnClickListener {
            onBackPressed()
        }

        val productName = intent.getStringExtra("product_name")
        val productPrice = intent.getStringExtra("product_price")
        val productImage = intent.getStringExtra("product_image")
        productQuantity = intent.getIntExtra("product_quantity", 1)
        pricePerItem = intent.getStringExtra("price_per_item")?.toIntOrNull() ?: 0


        binding.textNamaBuah.text = productName
        binding.textHarga.text = pricePerItem.toCurrencyFormat()
        Glide.with(this).load(productImage).into(binding.ivFruit)
        binding.textQty.text = "x$productQuantity"
        binding.tvTotalHarga.text = productPrice

        val currentDate = getCurrentDate()
        binding.textTanggal.text = currentDate

        binding.buttonPesan.setOnClickListener {
            val nama = binding.editTextNamaPenerima.text.toString()
            val alamat = binding.editTextAlamat.text.toString()

            if (nama.isEmpty() || alamat.isEmpty()) {
                Toast.makeText(this, "Nama dan Alamat tidak boleh kosong!", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val order = Order(
                nama_buah = productName ?: "",
                harga = productPrice ?: "",
                nama_penerima = nama,
                alamat = alamat,
                tanggal = currentDate,
                jumlah_barang = productQuantity
            )

            inputOrder(order)
        }
    }

    private fun inputOrder(order: Order) {
        val apiService = ApiClient.client?.create(ApiService::class.java)
        val call: Call<Void>? = apiService?.inputOrder(order)

        call?.enqueue(object : Callback<Void> {

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@OrderActivity, "Order berhasil", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@OrderActivity, "Gagal melakukan order", Toast.LENGTH_SHORT)
                        .show()
                    Log.e(
                        "OrderActivity",
                        "Failed to place order: ${response.errorBody()?.string()}"
                    )
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(
                    this@OrderActivity,
                    "Gagal melakukan order: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("OrderActivity", "Error placing order", t)
            }
        })
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return sdf.format(Date())
    }
}
