package com.group4.fruitopia.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast
import com.group4.fruitopia.R
import com.group4.fruitopia.data.model.Product
import com.group4.fruitopia.data.service.ApiClient
import com.group4.fruitopia.data.service.ApiService
import com.group4.fruitopia.home.adapter.AdapterLayoutMode
import com.group4.fruitopia.home.adapter.ProductAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var productAdapter: ProductAdapter
    private val productList: MutableList<Product> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_product_home)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false) // Horizontal LinearLayoutManager

        productAdapter = ProductAdapter(productList, requireContext(), AdapterLayoutMode.GRID)
        recyclerView.adapter = productAdapter

        fetchProduct()

        val ivSeeAll: ImageView = view.findViewById(R.id.iv_see_all)
        ivSeeAll.setOnClickListener {
            val intent = Intent(activity, SeeAllProductActivity::class.java)
            startActivity(intent)
        }

        return view
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
                    Toast.makeText(requireContext(), "Failed to retrieve data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
