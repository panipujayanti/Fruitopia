package com.group4.fruitopia.detailproduct

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.group4.fruitopia.R
import com.group4.fruitopia.order.OrderActivity
import com.group4.fruitopia.utils.toCurrencyFormat

class DetailProductActivity : AppCompatActivity() {

    private var quantity: Int = 1
    private var pricePerItem: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)

        val ivBack: ImageView = findViewById(R.id.iv_back)
        ivBack.setOnClickListener {
            onBackPressed()
        }

        val productName: String? = intent.getStringExtra("product_name")
        val productPrice: String? = intent.getStringExtra("product_price")
        val productImage: String? = intent.getStringExtra("product_image")

        val ivProduct: ImageView = findViewById(R.id.iv_product)
        val tvPriceProductCalculate: TextView = findViewById(R.id.tv_price_product_calculate)
        val tvProduct: TextView = findViewById(R.id.tv_product)
        val tvProductCount: TextView = findViewById(R.id.tv_product_count)
        val ivMinus: ImageView = findViewById(R.id.iv_minus)
        val ivPlus: ImageView = findViewById(R.id.iv_plus)

        tvProduct.text = productName

        pricePerItem = productPrice?.replace(Regex("[^\\d]"), "")?.toIntOrNull() ?: 0

        tvPriceProductCalculate.text = calculateTotalPrice()

        Glide.with(this).load(productImage).into(ivProduct)

        ivMinus.setOnClickListener {
            if (quantity > 1) {
                quantity--
                tvProductCount.text = quantity.toString()
                tvPriceProductCalculate.text = calculateTotalPrice()
            }
        }

        ivPlus.setOnClickListener {
            quantity++
            tvProductCount.text = quantity.toString()
            tvPriceProductCalculate.text = calculateTotalPrice()
        }

        val addToCartButton: Button = findViewById(R.id.button_add_to_cart)
        addToCartButton.setOnClickListener {
            navigateToOrderActivity(productName, productPrice, productImage)
        }
    }

    private fun navigateToOrderActivity(productName: String?, productPrice: String?, productImage: String?) {
        val totalPrice = calculateTotalPrice()
        val intent = Intent(this, OrderActivity::class.java).apply {
            putExtra("product_name", productName)
            putExtra("product_price", totalPrice)
            putExtra("product_image", productImage)
            putExtra("product_quantity", quantity)
            putExtra("price_per_item", pricePerItem.toString())
        }
        startActivity(intent)
        finish()
    }

    private fun calculateTotalPrice(): String {
        val totalPrice = quantity * pricePerItem
        return totalPrice.toCurrencyFormat()
    }
}
