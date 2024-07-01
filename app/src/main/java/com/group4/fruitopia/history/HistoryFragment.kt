package com.group4.fruitopia.history

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.group4.fruitopia.R
import com.group4.fruitopia.data.model.Transaction
import com.group4.fruitopia.data.service.ApiClient
import com.group4.fruitopia.data.service.ApiService
import com.group4.fruitopia.history.adapter.TransactionAdapter
import com.group4.fruitopia.order.OrderActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryFragment : Fragment(), TransactionAdapter.UpdateButtonClickListener,
    TransactionAdapter.DeleteButtonClickListener {

    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        recyclerView = view.findViewById(R.id.rv_product)
        recyclerView.layoutManager = LinearLayoutManager(context)

        fetchTransactions()

        return view
    }

    private fun fetchTransactions() {
        val apiService = ApiClient.client?.create(ApiService::class.java)
        apiService?.getTransactions()?.enqueue(object : Callback<List<Transaction>> {
            override fun onResponse(
                call: Call<List<Transaction>>,
                response: Response<List<Transaction>>
            ) {
                if (response.isSuccessful) {
                    val transactionList = response.body()
                    if (transactionList != null) {
                        transactionAdapter = TransactionAdapter(
                            transactionList,
                            this@HistoryFragment,
                            this@HistoryFragment
                        )
                        recyclerView.adapter = transactionAdapter
                    }
                } else {
                    Toast.makeText(context, "Failed to fetch transactions", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<List<Transaction>>, t: Throwable) {
                Toast.makeText(context, "Failed to fetch transactions", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onUpdateButtonClick(transaction: Transaction) {
        val intent = Intent(requireContext(), OrderActivity::class.java).apply {
            putExtra("transaction_id", transaction.id)
            putExtra("product_name", transaction.nama_buah)
            putExtra("product_image", transaction.product_image)
            putExtra("price_per_item", transaction.price_per_item)
            putExtra("product_price", transaction.harga)
            putExtra("product_quantity", transaction.jumlah_barang)
            putExtra("nama_penerima", transaction.nama_penerima)
            putExtra("alamat", transaction.alamat)
            putExtra("tanggal", transaction.tanggal)
        }
        startActivity(intent)
    }

    override fun onDeleteButtonClick(transaction: Transaction) {
        val apiService = ApiClient.client?.create(ApiService::class.java)
        apiService?.deleteTransaction(transaction.id)?.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Transaction deleted successfully", Toast.LENGTH_SHORT)
                        .show()
                    fetchTransactions()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(
                        context,
                        "Failed to delete transaction: $errorBody",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(
                    context,
                    "Failed to delete transaction: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        fetchTransactions()
    }
}
