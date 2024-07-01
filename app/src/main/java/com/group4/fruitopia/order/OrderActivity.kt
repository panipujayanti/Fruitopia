package com.group4.fruitopia.order

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.group4.fruitopia.data.model.Order
import com.group4.fruitopia.data.service.ApiClient
import com.group4.fruitopia.data.service.ApiService
import com.group4.fruitopia.databinding.ActivityOrderBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.group4.fruitopia.R
import com.group4.fruitopia.utils.toCurrencyFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding
    private var transactionId: Int = -1
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

        transactionId = intent.getIntExtra("transaction_id", -1)
        val productName = intent.getStringExtra("product_name")
        val productImage = intent.getStringExtra("product_image")
        val pricePerItem = intent.getStringExtra("price_per_item")?.toIntOrNull() ?: 0
        val productPrice = intent.getStringExtra("product_price")
        productQuantity = intent.getIntExtra("product_quantity", 1)
        val namaPenerima = intent.getStringExtra("nama_penerima")
        val alamat = intent.getStringExtra("alamat")
        val tanggal = intent.getStringExtra("tanggal")

        binding.textNamaBuah.text = productName
        binding.textHarga.text = pricePerItem.toCurrencyFormat()
        Glide.with(this).load(productImage).into(binding.ivFruit)
        binding.textQty.text = "x$productQuantity"
        binding.tvTotalHarga.text = productPrice

        val currentDate = getCurrentDate()
        binding.textTanggal.text = currentDate
        binding.editTextNamaPenerima.setText(namaPenerima)
        binding.editTextAlamat.setText(alamat)

        binding.buttonPesan.text =
            if (transactionId != -1) getString(R.string.text_update) else getString(R.string.text_order)
        binding.tvHeaderCheckout.text =
            if (transactionId != -1) getString(R.string.text_header_update) else getString(R.string.text_header_checkout)
        if (transactionId != -1) {
            binding.ivFruit.visibility = View.GONE
        } else {
            binding.ivFruit.visibility = View.VISIBLE
        }

        binding.buttonPesan.setOnClickListener {
            val updatedNama = binding.editTextNamaPenerima.text.toString()
            val updatedAlamat = binding.editTextAlamat.text.toString()

            if (updatedNama.isEmpty() || updatedAlamat.isEmpty()) {
                Toast.makeText(this, "Nama dan Alamat tidak boleh kosong!", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val order = Order(
                id = if (transactionId != -1) transactionId else null,
                nama_buah = productName ?: "",
                harga = productPrice ?: "",
                nama_penerima = updatedNama,
                alamat = updatedAlamat,
                tanggal = currentDate,
                jumlah_barang = productQuantity,
                product_image = productImage.toString(),
                price_per_item = pricePerItem.toString()
            )

            if (transactionId != -1) {
                updateOrder(order)
            } else {
                inputOrder(order)
            }
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
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(
                    this@OrderActivity,
                    "Gagal melakukan order: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun updateOrder(order: Order) {
        val apiService = ApiClient.client?.create(ApiService::class.java)
        val call: Call<Void>? = apiService?.updateOrder(order)

        call?.enqueue(object : Callback<Void> {

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@OrderActivity, "Update order berhasil", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                } else {
                    Toast.makeText(
                        this@OrderActivity,
                        "Gagal melakukan update order",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(
                    this@OrderActivity,
                    "Gagal melakukan update order: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return sdf.format(Date())
    }
}
