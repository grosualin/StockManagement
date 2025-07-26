package ro.alingrosu.stockmanagement.presentation.ui.main.transaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ro.alingrosu.stockmanagement.databinding.ItemTransactionBinding
import ro.alingrosu.stockmanagement.presentation.model.TransactionUi

class TransactionAdapter : ListAdapter<TransactionUi, RecyclerView.ViewHolder>(TransactionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TransactionViewHolder).bind(getItem(position))
    }

    class TransactionViewHolder(private val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TransactionUi) {
            binding.apply {
                tvProductName.text = item.product.name
                tvTransactionDescription.text = item.notes
                tvQtyCount.text = item.quantity.toString()
                tvTypeValue.text = item.type.value.uppercase()
            }
        }
    }

    class TransactionDiffCallback : DiffUtil.ItemCallback<TransactionUi>() {
        override fun areItemsTheSame(oldItem: TransactionUi, newItem: TransactionUi): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TransactionUi, newItem: TransactionUi): Boolean {
            return oldItem == newItem
        }
    }
}