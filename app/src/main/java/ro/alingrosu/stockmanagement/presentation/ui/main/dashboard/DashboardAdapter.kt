package ro.alingrosu.stockmanagement.presentation.ui.main.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ro.alingrosu.stockmanagement.R
import ro.alingrosu.stockmanagement.databinding.ItemLowStockProductBinding
import ro.alingrosu.stockmanagement.databinding.ItemRecentTransactionBinding
import ro.alingrosu.stockmanagement.presentation.model.DashboardListItem

class DashboardAdapter(
    private val onItemClicked: (DashboardListItem) -> Unit
) : ListAdapter<DashboardListItem, RecyclerView.ViewHolder>(DashboardDiffCallback()) {

    enum class ViewType(val id: Int) {
        PRODUCT(0), TRANSACTION(1)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DashboardListItem.ProductListItem -> ViewType.PRODUCT.id
            is DashboardListItem.TransactionListItem -> ViewType.TRANSACTION.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.PRODUCT.id -> {
                val binding = ItemLowStockProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ProductViewHolder(binding)
            }

            else -> {
                val binding = ItemRecentTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TransactionViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val model = getItem(position)) {
            is DashboardListItem.ProductListItem -> (holder as ProductViewHolder).bind(model, onItemClicked)
            is DashboardListItem.TransactionListItem -> (holder as TransactionViewHolder).bind(model, onItemClicked)
        }
    }

    class ProductViewHolder(private val binding: ItemLowStockProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DashboardListItem.ProductListItem, onItemClicked: (DashboardListItem) -> Unit) {
            binding.apply {
                root.setOnClickListener { onItemClicked.invoke(item) }
                tvProductName.text = item.product.name
                tvProductDescription.text = item.product.description
                tvStockCount.text = item.product.currentStock.toString()
                val color = if (item.product.currentStock < item.product.minStock) R.color.red else R.color.black
                tvStockCount.setTextColor(ContextCompat.getColor(binding.root.context, color))
                tvMinStockCount.text = item.product.minStock.toString()
            }
        }
    }

    class TransactionViewHolder(private val binding: ItemRecentTransactionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DashboardListItem.TransactionListItem, onItemClicked: (DashboardListItem) -> Unit) {
            binding.apply {
                root.setOnClickListener { onItemClicked.invoke(item) }
                tvProductName.text = item.transaction.product.name
                tvTransactionDescription.text = item.transaction.notes
                tvQtyCount.text = item.transaction.quantity.toString()
                tvTypeValue.text = item.transaction.type.value
            }
        }
    }

    class DashboardDiffCallback : DiffUtil.ItemCallback<DashboardListItem>() {
        override fun areItemsTheSame(oldItem: DashboardListItem, newItem: DashboardListItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DashboardListItem, newItem: DashboardListItem): Boolean {
            return oldItem == newItem
        }
    }

}