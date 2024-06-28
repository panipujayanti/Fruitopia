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

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val productName = intent.getStringExtra("product_name")
        val productPrice = intent.getStringExtra("product_price")

        binding.textNamaBuah.text = productName
        binding.textHarga.text = productPrice

        binding.buttonPesan.setOnClickListener {
            val nama = binding.editTextNamaPenerima.text.toString()
            val alamat = binding.editTextAlamat.text.toString()
            val tanggal = getSelectedDate() // Mendapatkan tanggal dari CalendarView

            if (nama.isEmpty() || alamat.isEmpty() || tanggal.isEmpty()) {
                Toast.makeText(this, "Nama, Alamat, dan Tanggal harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val order = Order(
                nama_buah = productName ?: "",
                harga = productPrice ?: "",
                nama_penerima = nama,
                alamat = alamat,
                tanggal = tanggal
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
                    Toast.makeText(this@OrderActivity, "Gagal melakukan order", Toast.LENGTH_SHORT).show()
                    Log.e("OrderActivity", "Failed to place order: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@OrderActivity, "Gagal melakukan order: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("OrderActivity", "Error placing order", t)
            }
        })
    }

    // Fungsi untuk mendapatkan tanggal yang dipilih dari CalendarView
    private fun getSelectedDate(): String {
        val calendarView = binding.calender
        val date = Date(calendarView.date)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(date)
    }
}
