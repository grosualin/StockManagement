package ro.alingrosu.stockmanagement.domain.model

data class Product(
    val id: Int = 0,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val barcode: String,
    val supplierId: Int,
    val currentStock: Int,
    val minStock: Int
)