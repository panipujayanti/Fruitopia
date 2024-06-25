package com.group4.fruitopia.home

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.group4.fruitopia.R
import com.group4.fruitopia.data.model.Product
import com.group4.fruitopia.data.service.ApiClient
import com.group4.fruitopia.data.service.ApiService
import com.group4.fruitopia.home.adapter.AdapterLayoutMode
import com.group4.fruitopia.home.adapter.ProductAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SeeAllProductActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private val productList: MutableList<Product> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_all_product)

        val ivBack: ImageView = findViewById(R.id.iv_back)
        ivBack.setOnClickListener {
            onBackPressed()
        }

        recyclerView = findViewById(R.id.rv_product)
        val gridLayoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = gridLayoutManager

        productAdapter = ProductAdapter(productList, this, AdapterLayoutMode.GRID)
        recyclerView.adapter = productAdapter

        fetchProduct()
    }

    private fun fetchProduct() {
        val apiService = ApiClient.client.create(ApiService::class.java)
        val call = apiService.getProduct()
        call.enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful && response.body() != null) {
                    productList.clear()
                    productList.addAll(response.body()!!)
                    productAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@SeeAllProductActivity, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(this@SeeAllProductActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
