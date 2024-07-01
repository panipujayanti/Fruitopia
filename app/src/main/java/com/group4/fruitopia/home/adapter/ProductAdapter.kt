package com.group4.fruitopia.home.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.group4.fruitopia.R
import com.group4.fruitopia.data.model.Product
import com.group4.fruitopia.detailproduct.DetailProductActivity
import com.group4.fruitopia.utils.toCurrencyFormat
import com.squareup.picasso.Picasso

class ProductAdapter(
    private var productList: MutableList<Product>,
    private val context: Context,
    private val adapterLayoutMode: AdapterLayoutMode
) : RecyclerView.Adapter<ProductAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutRes = if (adapterLayoutMode == AdapterLayoutMode.GRID) {
            R.layout.item_menu
        } else {
            R.layout.item_transaction
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val product = productList[position]
        holder.namaBuah.text = product.namaBuah

        val priceInt = product.harga.replace(Regex("[^\\d]"), "").toIntOrNull()
        holder.harga.text = priceInt?.toCurrencyFormat() ?: product.harga

        Picasso.get().load(product.img).into(holder.img)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailProductActivity::class.java).apply {
                putExtra("product_id", product.id)
                putExtra("product_name", product.namaBuah)
                putExtra("product_price", product.harga)
                putExtra("product_image", product.img)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = productList.size

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaBuah: TextView = itemView.findViewById(R.id.tv_product)
        val harga: TextView = itemView.findViewById(R.id.tv_price_product)
        val img: ImageView = itemView.findViewById(R.id.iv_grid_menu)
    }

    fun updateProducts(products: List<Product>) {
        productList.clear()
        productList.addAll(products)
        notifyDataSetChanged()
    }
}
