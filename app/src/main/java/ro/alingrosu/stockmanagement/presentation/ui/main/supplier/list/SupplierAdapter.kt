package ro.alingrosu.stockmanagement.presentation.ui.main.supplier.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ro.alingrosu.stockmanagement.databinding.ItemSupplierBinding
import ro.alingrosu.stockmanagement.presentation.model.SupplierUi

class SupplierAdapter(
    private val onItemClicked: (SupplierUi) -> Unit
) : ListAdapter<SupplierUi, RecyclerView.ViewHolder>(SupplierDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemSupplierBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SupplierViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SupplierViewHolder).bind(getItem(position), onItemClicked)
    }

    class SupplierViewHolder(private val binding: ItemSupplierBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SupplierUi, onItemClicked: (SupplierUi) -> Unit) {
            binding.apply {
                root.setOnClickListener { onItemClicked.invoke(item) }
                tvSupplierName.text = item.name
                tvSupplierAddress.text = item.address
            }
        }
    }

    class SupplierDiffCallback : DiffUtil.ItemCallback<SupplierUi>() {
        override fun areItemsTheSame(oldItem: SupplierUi, newItem: SupplierUi): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SupplierUi, newItem: SupplierUi): Boolean {
            return oldItem == newItem
        }
    }
}