package ro.alingrosu.stockmanagement.presentation.model

import java.io.Serializable

data class ProductUi(
    val id: Int = 0,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val barcode: String,
    val supplierId: Int,
    val currentStock: Int,
    val minStock: Int
): Serializable