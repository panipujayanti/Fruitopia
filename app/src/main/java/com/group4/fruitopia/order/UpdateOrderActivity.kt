package com.group4.fruitopia.order

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.group4.fruitopia.R
import com.group4.fruitopia.data.model.Order
import com.group4.fruitopia.data.service.ApiClient
import com.group4.fruitopia.data.service.ApiService
import com.group4.fruitopia.databinding.ActivityUpdateOrderBinding
import com.group4.fruitopia.utils.toCurrencyFormat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UpdateOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateOrderBinding
    private var transactionId: Int = -1
    private var productQuantity: Int = 1
    private var pricePerItem: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transactionId = intent.getIntExtra("transaction_id", -1)
        val productName = intent.getStringExtra("product_name")
        pricePerItem = intent.getStringExtra("price_per_item")?.toIntOrNull() ?: 0
        val productPrice = intent.getStringExtra("product_price")
        productQuantity = intent.getIntExtra("product_quantity", 1)
        val namaPenerima = intent.getStringExtra("nama_penerima")
        val alamat = intent.getStringExtra("alamat")
        val tanggal = intent.getStringExtra("tanggal")

        binding.textNamaBuah.text = productName
        binding.textHarga.text = pricePerItem.toCurrencyFormat()
        binding.textQty.text = productQuantity.toString()
        binding.tvTotalHarga.text = productPrice

        val currentDate = getCurrentDate()
        binding.textTanggal.text = currentDate
        binding.editTextNamaPenerima.setText(namaPenerima)
        binding.editTextAlamat.setText(alamat)

        binding.buttonUpdate.setOnClickListener {
            val updatedNama = binding.editTextNamaPenerima.text.toString()
            val updatedAlamat = binding.editTextAlamat.text.toString()

            if (updatedNama.isEmpty() || updatedAlamat.isEmpty()) {
                Toast.makeText(this, "Nama dan alamat tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val order = Order(
                id = transactionId,
                nama_buah = productName ?: "",
                harga = productPrice ?: "",
                nama_penerima = updatedNama,
                alamat = updatedAlamat,
                tanggal = currentDate,
                jumlah_barang = productQuantity,
                price_per_item = pricePerItem.toString()
            )
            showUpdateConfirmationDialog(order)
        }
    }

    private fun showUpdateConfirmationDialog(order: Order) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Konfirmasi")
            .setMessage("Apakah Anda yakin ingin mengubah data pesanan ini?")
            .setPositiveButton("Ya") { dialog, which ->
                updateOrder(order)
            }
            .setNegativeButton("Tidak") { dialog, which ->
                dialog.dismiss()
                finish()
            }

        val alertDialog = builder.create()

        // Terapkan gaya pada tombol-tombol
        alertDialog.setOnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(resources.getColor(R.color.app_color_on_primary))
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(resources.getColor(R.color.app_color_on_primary))
        }

        alertDialog.show()
    }


    private fun updateOrder(order: Order) {
        val apiService = ApiClient.client?.create(ApiService::class.java)
        val call: Call<Void>? = apiService?.updateOrder(order)

        call?.enqueue(object : Callback<Void> {

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@UpdateOrderActivity, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@UpdateOrderActivity, "Gagal memperbarui data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@UpdateOrderActivity, "Gagal memperbarui data: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return sdf.format(Date())
    }
}
