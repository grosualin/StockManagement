package ro.alingrosu.stockmanagement.presentation.ui.main.product.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ro.alingrosu.stockmanagement.R
import ro.alingrosu.stockmanagement.databinding.ItemProductBinding
import ro.alingrosu.stockmanagement.presentation.model.ProductUi

class ProductAdapter(
    private val onItemClicked: (ProductUi) -> Unit
) : ListAdapter<ProductUi, RecyclerView.ViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ProductViewHolder).bind(getItem(position), onItemClicked)
    }

    class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductUi, onItemClicked: (ProductUi) -> Unit) {
            binding.apply {
                root.setOnClickListener { onItemClicked.invoke(item) }
                tvProductName.text = item.name
                tvProductDescription.text = item.description
                tvCategoryValue.text = item.category
                tvSupplierValue.text = item.supplier.name
                tvPriceValue.text = "$${item.price}"
                tvStockCount.text = item.currentStock.toString()
                val color = if (item.currentStock < item.minStock) R.color.red else R.color.black
                tvStockCount.setTextColor(ContextCompat.getColor(binding.root.context, color))
                tvMinStockCount.text = item.minStock.toString()
            }
        }
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<ProductUi>() {
        override fun areItemsTheSame(oldItem: ProductUi, newItem: ProductUi): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductUi, newItem: ProductUi): Boolean {
            return oldItem == newItem
        }
    }
}