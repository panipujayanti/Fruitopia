package com.group4.fruitopia.history.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.group4.fruitopia.R
import com.group4.fruitopia.data.model.Transaction

class TransactionAdapter(
    private val transactionList: List<Transaction>,
    private val updateButtonClickListener: UpdateButtonClickListener,
    private val deleteButtonClickListener: DeleteButtonClickListener
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]

        holder.bind(transaction)
        holder.buttonUpdate.setOnClickListener {
            updateButtonClickListener.onUpdateButtonClick(transaction)
        }
        holder.buttonDelete.setOnClickListener {
            deleteButtonClickListener.onDeleteButtonClick(transaction)
        }
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val namaBuah: TextView = itemView.findViewById(R.id.tv_nama_buah)
        private val totalQuantity: TextView = itemView.findViewById(R.id.tv_total_quantity)
        private val pricePerItem: TextView = itemView.findViewById(R.id.tv_price_per_item)
        private val totalPrice: TextView = itemView.findViewById(R.id.tv_total_price)
        private val namaPenerima: TextView = itemView.findViewById(R.id.tv_nama_penerima)
        private val alamat: TextView = itemView.findViewById(R.id.tv_alamat)
        private val tanggal: TextView = itemView.findViewById(R.id.tv_date)
        val buttonUpdate: Button = itemView.findViewById(R.id.button_update)
        val buttonDelete: Button = itemView.findViewById(R.id.button_delete)

        fun bind(transaction: Transaction) {
            namaBuah.text = transaction.nama_buah
            totalQuantity.text = transaction.jumlah_barang.toString()
            pricePerItem.text = transaction.price_per_item
            totalPrice.text = transaction.harga
            namaPenerima.text = transaction.nama_penerima
            alamat.text = transaction.alamat
            tanggal.text = transaction.tanggal

            buttonUpdate.setOnClickListener {
                updateButtonClickListener.onUpdateButtonClick(transaction)
            }
            buttonDelete.setOnClickListener {
                deleteButtonClickListener.onDeleteButtonClick(transaction)
            }
        }
    }

    interface UpdateButtonClickListener {
        fun onUpdateButtonClick(transaction: Transaction)
    }

    interface DeleteButtonClickListener {
        fun onDeleteButtonClick(transaction: Transaction)
    }
}
