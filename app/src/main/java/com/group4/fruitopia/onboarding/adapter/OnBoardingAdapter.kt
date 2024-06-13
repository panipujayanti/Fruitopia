package com.group4.fruitopia.onboarding.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.group4.fruitopia.R

class OnBoardingAdapter(
    private val context: Context
) : RecyclerView.Adapter<OnBoardingAdapter.ViewHolder>() {

    private val onBoardingItems = listOf(
        Triple("KUALITAS TERBAIK", "Kami hanya menyediakan \n buah-buahan berkualitas terbaik", R.drawable.background_on_boarding_1),
        Triple("PROMO SPESIAL", "Dapatkan promo spesial dan diskon \n menarik setiap hari", R.drawable.background_on_boarding_2),
        Triple("MULAI BELANJA", "Siap menikmati buah segar hari ini? \n Mari mulai belanja di FRUITOPIA sekarang!", R.drawable.background_on_boarding_3)
    )

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.iv_on_boarding)
        val titleTextView: TextView = itemView.findViewById(R.id.tv_desk_1)
        val descriptionTextView: TextView = itemView.findViewById(R.id.tv_desk_2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_on_boarding, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (title, description, imageResId) = onBoardingItems[position]
        holder.imageView.setImageResource(imageResId)
        holder.titleTextView.text = title
        holder.descriptionTextView.text = description
    }

    override fun getItemCount(): Int {
        return onBoardingItems.size
    }
}
