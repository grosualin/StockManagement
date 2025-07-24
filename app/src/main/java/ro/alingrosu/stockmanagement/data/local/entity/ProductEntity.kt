package ro.alingrosu.stockmanagement.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val barcode: String,
    val supplierId: Int,
    val currentStock: Int,
    val minStock: Int
)